package oscar.oscarLab.ca.all.parsers;
import ca.uhn.hl7v2.HL7Exception;
import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.model.Segment;
import ca.uhn.hl7v2.model.Structure;
import ca.uhn.hl7v2.parser.EncodingCharacters;
import ca.uhn.hl7v2.parser.EncodingNotSupportedException;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.FilterIterator;
import ca.uhn.hl7v2.util.MessageIterator;
import ca.uhn.log.HapiLog;
import ca.uhn.log.HapiLogFactory;


public class MdsParser extends PipeParser
{
    private static final HapiLog log = HapiLogFactory.getHapiLog(MdsParser.class);
    private final static String segDelim = "\r"; //see section 2.8 of spec

    /**
     * The only change to the PipeParser method is that if it ends in 0 truncate it,
     * i.e. 2.3.0 is not valid, it should have been just 2.3
     */
	@Override
	public String getVersion(String message) throws HL7Exception {
		String version=super.getVersion(message);		
		if (version.endsWith(".0")) version=version.substring(0, version.length()-2);
		return(version);
	}
	
	/**
	 * The only change from the PipeParser method is the removal of requirement for event / structure.
	 * i.e. if you look down at the code the big commented out block that use to say you needed 3 components, the
	 * (message, event, structure), now if we only have the message, i.e. ORU, I will return that
	 * and just hope for the best with the event and structure.
	 */
    private MessageStructure getStructure(String message) throws HL7Exception, EncodingNotSupportedException {
        EncodingCharacters ec = getEncodingChars(message);
        String messageStructure = null;
        boolean explicityDefined = true;
        String wholeFieldNine;
        try {
            String[] fields = split(message.substring(0, Math.max(message.indexOf(segDelim), message.length())),
                String.valueOf(ec.getFieldSeparator()));
            wholeFieldNine = fields[8];
            
            //message structure is component 3 but we'll accept a composite of 1 and 2 if there is no component 3 ...
            //      if component 1 is ACK, then the structure is ACK regardless of component 2
            String[] comps = split(wholeFieldNine, String.valueOf(ec.getComponentSeparator()));
            if (comps.length >= 3) {
                messageStructure = comps[2];
            } else if (comps.length > 0 && comps[0] != null && comps[0].equals("ACK")) {
                messageStructure = "ACK";
            } else if (comps.length == 2) {
                explicityDefined = false;
                messageStructure = comps[0] + "_" + comps[1];
            }
            /*else if (comps.length == 1 && comps[0] != null && comps[0].equals("ACK")) {
                messageStructure = "ACK"; //it's common for people to only populate component 1 in an ACK msg
            }*/
            else {
// The following is the original code from PipeParser which is commented out for MDS purposes... because MDS files only ever have the message type and never the event & structure
// This is left here so if we have to update this file from a new PipeParser some one will know what to change.
//---------            	
//                StringBuilder buf = new StringBuilder("Can't determine message structure from MSH-9: ");
//                buf.append(wholeFieldNine);
//                if (comps.length < 3) {
//                    buf.append(" HINT: there are only ");
//                    buf.append(comps.length);
//                    buf.append(" of 3 components present");
//                }
//                
//                throw new HL7Exception(buf.toString(), HL7Exception.UNSUPPORTED_MESSAGE_TYPE);

            	messageStructure=comps[0];
            }            
        }
        catch (IndexOutOfBoundsException e) {
            throw new HL7Exception(
            "Can't find message structure (MSH-9-3): " + e.getMessage(),
            HL7Exception.UNSUPPORTED_MESSAGE_TYPE);
        }
        
        return new MessageStructure(messageStructure, explicityDefined);
    }	

    /**
	 * Copied verbatim from PipeParser because of private access
     */
    private static EncodingCharacters getEncodingChars(String message) {
        return new EncodingCharacters(message.charAt(3), message.substring(4, 8));
    }

    /**
	 * Copied verbatim from PipeParser because of private access
     */
    private static class MessageStructure {
        public String messageStructure;
        public boolean explicitlyDefined;
        
        public MessageStructure(String theMessageStructure, boolean isExplicitlyDefined) {
            messageStructure = theMessageStructure;
            explicitlyDefined = isExplicitlyDefined;
        }
    }

	
    /**
	 * Copied verbatim from PipeParser because of private access
	 */
    @Override
	public String getMessageStructure(String message) throws HL7Exception, EncodingNotSupportedException {
        return getStructure(message).messageStructure;
    }

    /**
	 * Copied verbatim from PipeParser because of private access method call within
	 */
    @Override
	protected Message doParse(String message, String version) throws HL7Exception, EncodingNotSupportedException {
        
        //try to instantiate a message object of the right class
        MessageStructure structure = getStructure(message);
        Message m = instantiateMessage(structure.messageStructure, version, structure.explicitlyDefined);
        
        //MessagePointer ptr = new MessagePointer(this, m, getEncodingChars(message));
        MessageIterator messageIter = new MessageIterator(m, "MSH", true);
        FilterIterator.Predicate segmentsOnly = new FilterIterator.Predicate() {
            public boolean evaluate(Object obj) {
                if (Segment.class.isAssignableFrom(obj.getClass())) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        FilterIterator segmentIter = new FilterIterator(messageIter, segmentsOnly);
        
        String[] segments = split(message, segDelim);

        char delim = '|';
        for (int i = 0; i < segments.length; i++) {
            
            //get rid of any leading whitespace characters ...
            if (segments[i] != null && segments[i].length() > 0 && Character.isWhitespace(segments[i].charAt(0)))
                segments[i] = stripLeadingWhitespace(segments[i]);
            
            //sometimes people put extra segment delimiters at end of msg ...
            if (segments[i] != null && segments[i].length() >= 3) {
                final String name;
                if (i == 0) {
                    name = segments[i].substring(0, 3);
                    delim = segments[i].charAt(3);
                } else {
                    if (segments[i].indexOf(delim) >= 0 ) {
                        name = segments[i].substring(0, segments[i].indexOf(delim));
                      } else {
                        name = segments[i];
                      }
                 }
                
                log.debug("Parsing segment " + name);
                
                messageIter.setDirection(name);
                FilterIterator.Predicate byDirection = new FilterIterator.Predicate() {
                    public boolean evaluate(Object obj) {
                        Structure s = (Structure) obj;
                        log.debug("PipeParser iterating message in direction " + name + " at " + s.getName());
                        if (s.getName().matches(name + "\\d*")) {
                            return true;
                        } else {
                            return false;
                        }
                    }                    
                };
                FilterIterator dirIter = new FilterIterator(segmentIter, byDirection);
                if (dirIter.hasNext()) {
                    parse((Segment) dirIter.next(), segments[i], getEncodingChars(message));
                }
            }
        }
        return m;
    }
}
