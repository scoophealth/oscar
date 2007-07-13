/* A custom set of validation rules */

package oscar.oscarLab.ca.all.upload;

import ca.uhn.hl7v2.validation.impl.ValidationContextImpl;
import ca.uhn.hl7v2.validation.impl.TrimLeadingWhitespace;
import ca.uhn.hl7v2.validation.impl.RuleBinding;
import ca.uhn.hl7v2.validation.impl.RegexPrimitiveRule;
import ca.uhn.hl7v2.validation.impl.SizeRule;
import ca.uhn.hl7v2.validation.Rule;

public class MyCustomValidation extends ValidationContextImpl {

	public MyCustomValidation() {
        Rule trim = new TrimLeadingWhitespace();
        getPrimitiveRuleBindings().add(new RuleBinding("*", "FT", trim));
        getPrimitiveRuleBindings().add(new RuleBinding("*", "ST", trim));
        getPrimitiveRuleBindings().add(new RuleBinding("*", "TX", trim));                 

        Rule size200 = new SizeRule(200);
        Rule size32000 = new SizeRule(32000);
        getPrimitiveRuleBindings().add(new RuleBinding("*", "FT", size32000));
        getPrimitiveRuleBindings().add(new RuleBinding("*", "ID", size200));
        getPrimitiveRuleBindings().add(new RuleBinding("*", "IS", size200));

        Rule nonNegativeInteger = new RegexPrimitiveRule("\\d*", "");
        getPrimitiveRuleBindings().add(new RuleBinding("*", "SI", nonNegativeInteger));                                    

		/* Default number rule
         * Rule number = new RegexPrimitiveRule("(\\+|\\-)?\\d*\\.?\\d*", ""); */
		
		/* A custum number rule to deal with numbers with the following changes
		 * Allows < and > 
		 * Allows NEG instead of a number 
		 * Allows SMALL instead of a number 
		 * Allows a range to be specified instead of a number (ie. 0-2)
		 * Allows TRACE instead of a number */
		Rule number = new RegexPrimitiveRule("(NEG|SMALL|TRACE|((\\<|\\>)?((\\+|\\-)?\\d*\\.?\\d*))|(((\\+|\\-)?\\d*\\.?\\d*)\\-?((\\+|\\-)?\\d*\\.?\\d*)))", "");
        getPrimitiveRuleBindings().add(new RuleBinding("*", "NM", number));
        
		/* Default telephoneNumber rule 
		 * Rule telephoneNumber 
         *  = new RegexPrimitiveRule("(\\d{1,2} )?(\\(\\d{3}\\))?\\d{3}-\\d{4}(X\\d{1,5})?(B\\d{1,5})?(C.*)?", 
         *      "Version 2.4 Section 2.9.45"); */

		/* A custum number rule to deal with telephone numbers with the following changes
		 * Allows the 1-555-555-5555 format to be used 
		 * Allows the 1 555 555 5555 format to be used
		 * Allows the 1.555.555.5555 format to be used
		 * Allows for combinations of the new formats and the original format 
		 *	
		 * The telephoneNumber rule is commented out to allow any types of telephone numbers 
		 *        
		Rule telephoneNumber 
            = new RegexPrimitiveRule("((\\d{1,2} )|(\\d{1}-)|(\\d{1}.))?((\\(\\d{3}\\))|(\\(\\d{3}\\) )|\\d{3}-|\\d{3}.|\\d{3} )?(\\d{3}-|\\d{3}.|\\d{3} )\\d{4}(X\\d{1,5})?(B\\d{1,5})?(C.*)?", 
                "Version 2.4 Section 2.9.45");
        getPrimitiveRuleBindings().add(new RuleBinding("*", "TN", telephoneNumber));        
		*/        
		
        String datePattern = "(\\d{4}([01]\\d(\\d{2})?)?)?"; //YYYY[MM[DD]]
        Rule date = new RegexPrimitiveRule(datePattern, "Version 2.5 Section 2.16.24");
        getPrimitiveRuleBindings().add(new RuleBinding("*", "DT", date));
        
        String timePattern  //HH[MM[SS[.S[S[S[S]]]]]][+/-ZZZZ] 
            = "([012]\\d([0-5]\\d([0-5]\\d(\\.\\d(\\d(\\d(\\d)?)?)?)?)?)?)?([\\+\\-]\\d{4})?";
        Rule time = new RegexPrimitiveRule(timePattern, "Version 2.5 Section 2.16.79");
        getPrimitiveRuleBindings().add(new RuleBinding("*", "TM", time));
                
        String datetimePattern   
            = "(\\d{4}([01]\\d(\\d{2}([012]\\d([0-5]\\d([0-5]\\d(\\.\\d(\\d(\\d(\\d)?)?)?)?)?)?)?)?)?)?([\\+\\-]\\d{4})?";
        Rule datetime = new RegexPrimitiveRule(datetimePattern, "Version 2.5 Section 2.16.25");
        getPrimitiveRuleBindings().add(new RuleBinding("*", "TSComponentOne", datetime));
        getPrimitiveRuleBindings().add(new RuleBinding("*", "DTM", datetime));
    }
}