/**
 * Copyright (C) 2011-2012  PeaceWorks Technology Solutions
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 * 
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package org.oscarehr.oscarRx.erx.model;

import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFactory;

import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * A value type that stores the prescription data sent by the External Prescriber. 
 */
public class ERxPrescription {
	/**
	 * Physician's license number issued by a regulatory body.
	 * 
	 * Alphanumeric characters only.
	 */
	private String doctorLicenseNo;
	/**
	 * A unique identifier for the patient, as set by the External Prescription software
	 * 
	 * This field will match the WSPatientX.PatientId value (set when a patient
	 * is entered).
	 */
	private String patientId;
	/**
	 * A unique identifier for the prescription.
	 * 
	 * It is a string representation of a GUID.
	 */
	private String prescriptionId;
	/**
	 * TRUE if this prescription is of type Discontinuation/Ceasing; FALSE
	 * otherwise.
	 * 
	 * See also {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.statusID} =
	 * 3, 4.
	 * 
	 * @see org.oscarehr.oscarRx.erx.model.ERxPrescription.discontinuedUntil
	 * @see org.oscarehr.oscarRx.erx.model.ERxPrescription.discontinuedPrescriptionId
	 */
	private boolean isDCPrescription;
	/**
	 * References the original
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.prescriptionId} that
	 * is being discontinued/ceased.
	 * 
	 * Is only set if
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.isDCPrescription} is
	 * set to TRUE.
	 */
	private String discontinuedPrescriptionId;
	/**
	 * Specifies the date that the prescription will be re-activated on. If
	 * given, will be in the format "YYYY-MM-DD".
	 * 
	 * Is only set if the physician is temporarily stopping the given medication
	 * for a time. The medication should re-activate on the date given here.
	 * 
	 * Is set only if
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.isDCPrescription} is
	 * set to TRUE.
	 */
	private String discontinuedUntil;
	/**
	 * TRUE if the prescription is of type Renewal; FALSE otherwise.
	 * 
	 * See also {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.statusID} =
	 * 2.
	 * 
	 * @see org.oscarehr.oscarRx.erx.model.ERxPrescription.renewedPrescriptionId
	 */
	private boolean isRenewal;
	/**
	 * References the original
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.prescriptionId} that
	 * is being renewed.
	 * 
	 * Is only set if
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.isRenewal} is set to
	 * TRUE.
	 */
	private String renewedPrescriptionId;
	/**
	 * Represents the date the prescription was made by the physician. If given,
	 * will be in the format "YYYY-MM-DD".
	 */
	private String prescriptionDateTime;
	/**
	 * The (localized) brand name of the drug that is being prescribed.
	 */
	private String productName;
	/**
	 * The (localized) form in which the prescribed drug will be served.
	 * 
	 * Examples: tablet, ointment, spray, etc.
	 */
	private String productForm;
	/**
	 * The quantity of drug in its form.
	 * 
	 * Example: 100mg
	 */
	private String productStrength;
	/**
	 * A identification code for the drug.
	 * 
	 * In Canada, this value will be a DIN (Drug Identification Number), as
	 * defined by Health Canada.
	 * 
	 * In the United States, this value will be an NDC (National Drug Code), as
	 * defined by the Food and Drug Administration.
	 * 
	 * @see <a
	 *      href="http://www.hc-sc.gc.ca/dhp-mps/prodpharma/activit/fs-fi/dinfs_fd-eng.php">the
	 *      Health Canada website</a>
	 * @see <a
	 *      href="http://www.fda.gov/Drugs/InformationOnDrugs/ucm142438.htm">The
	 *      Food and Drug Administration (USA)'s website</a>
	 */
	private long drugCode;
	/**
	 * The quantity to dispense.
	 * 
	 * The field {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.qtyUnitId}
	 * refers to this field.
	 * 
	 * In some prescriptions, the
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.quantity} and
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.treatmentDuration}
	 * fields can both be set to 0. This happens when the prescribed medication
	 * is sold by the pharmacist in its original packaging (e.g.: Nasonex).
	 * Versions 1 and 2 of the the External Prescriber interface set
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.quantity} and
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.treatmentDuration} to
	 * an empty string in this case.
	 */
	private float quantity;
	/**
	 * The dispense quantity qualifier.
	 * 
	 * This field refers to both
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.quantity} and
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.narcoticTotalQuantity}
	 * fields.
	 * 
	 * A table of the possible values for this field and their corresponding
	 * interpretations are as follows:
	 * <table>
	 * <caption><b>Units of measure</b></caption> <thead>
	 * <tr>
	 * <th>UnitOfMeasureId</th>
	 * <th>SIG Unit (en)</th>
	 * <th>Qty Unit (en)</th>
	 * <th>Long Name (en)</th>
	 * <th>SIG Unit (fr)</th>
	 * <th>Qty Unit (fr)</th>
	 * <th>Long Name (fr)</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>1</td>
	 * <td>adapt.</td>
	 * <td>adapt.</td>
	 * <td>adapter</td>
	 * <td>embout</td>
	 * <td>embout</td>
	 * <td>embout</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>advice</td>
	 * <td>advice</td>
	 * <td>advice</td>
	 * <td>Opinion</td>
	 * <td>Opinion</td>
	 * <td>Opinion</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>app.</td>
	 * <td>app.</td>
	 * <td>app.</td>
	 * <td>app.</td>
	 * <td>app.</td>
	 * <td>app.</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>appl.</td>
	 * <td>appl.</td>
	 * <td>application</td>
	 * <td>appl.</td>
	 * <td>appl.</td>
	 * <td>application</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>bag</td>
	 * <td>bag</td>
	 * <td>bag</td>
	 * <td>sac</td>
	 * <td>sac</td>
	 * <td>sac</td>
	 * </tr>
	 * <tr>
	 * <td>6</td>
	 * <td>band.</td>
	 * <td>band.</td>
	 * <td>bandage</td>
	 * <td>band.</td>
	 * <td>band.</td>
	 * <td>bandage</td>
	 * </tr>
	 * <tr>
	 * <td>7</td>
	 * <td>bar</td>
	 * <td>bar</td>
	 * <td>bar</td>
	 * <td>barre</td>
	 * <td>barre</td>
	 * <td>barre</td>
	 * </tr>
	 * <tr>
	 * <td>8</td>
	 * <td>batt.</td>
	 * <td>batt.</td>
	 * <td>batterie</td>
	 * <td>pile</td>
	 * <td>pile</td>
	 * <td>pile</td>
	 * </tr>
	 * <tr>
	 * <td>9</td>
	 * <td>botl</td>
	 * <td>botl</td>
	 * <td>bottle</td>
	 * <td>bout.</td>
	 * <td>bout.</td>
	 * <td>bouteille</td>
	 * </tr>
	 * <tr>
	 * <td>10</td>
	 * <td>box</td>
	 * <td>box</td>
	 * <td>box</td>
	 * <td>bo&Atilde;&reg;te</td>
	 * <td>bo&Atilde;&reg;te</td>
	 * <td>bo&Atilde;&reg;te</td>
	 * </tr>
	 * <tr>
	 * <td>11</td>
	 * <td>cap.</td>
	 * <td>cap.</td>
	 * <td>caplet</td>
	 * <td>capl.</td>
	 * <td>capl.</td>
	 * <td>caplet</td>
	 * </tr>
	 * <tr>
	 * <td>12</td>
	 * <td>caps.</td>
	 * <td>caps.</td>
	 * <td>capsule</td>
	 * <td>caps.</td>
	 * <td>caps.</td>
	 * <td>capsule</td>
	 * </tr>
	 * <tr>
	 * <td>13</td>
	 * <td>cart.</td>
	 * <td>cart.</td>
	 * <td>cartridge</td>
	 * <td>cart.</td>
	 * <td>cart.</td>
	 * <td>cartouche</td>
	 * </tr>
	 * <tr>
	 * <td>14</td>
	 * <td>cont.</td>
	 * <td>cont.</td>
	 * <td>container</td>
	 * <td>cont.</td>
	 * <td>cont.</td>
	 * <td>contenant</td>
	 * </tr>
	 * <tr>
	 * <td>15</td>
	 * <td>dev.</td>
	 * <td>dev.</td>
	 * <td>device</td>
	 * <td>disp.</td>
	 * <td>disp.</td>
	 * <td>dispositif</td>
	 * </tr>
	 * <tr>
	 * <td>16</td>
	 * <td>dose</td>
	 * <td>dose</td>
	 * <td>dose</td>
	 * <td>dose</td>
	 * <td>dose</td>
	 * <td>dose</td>
	 * </tr>
	 * <tr>
	 * <td>17</td>
	 * <td>dres.</td>
	 * <td>dres.</td>
	 * <td>dressing</td>
	 * <td>pans.</td>
	 * <td>pans.</td>
	 * <td>pansement</td>
	 * </tr>
	 * <tr>
	 * <td>18</td>
	 * <td>drop</td>
	 * <td>drop</td>
	 * <td>drop</td>
	 * <td>gte</td>
	 * <td>gte</td>
	 * <td>goutte</td>
	 * </tr>
	 * <tr>
	 * <td>19</td>
	 * <td>ea.</td>
	 * <td>ea.</td>
	 * <td>each</td>
	 * <td>chac.</td>
	 * <td>chac.</td>
	 * <td>chacun</td>
	 * </tr>
	 * <tr>
	 * <td>20</td>
	 * <td>film</td>
	 * <td>film</td>
	 * <td>film</td>
	 * <td>film</td>
	 * <td>film</td>
	 * <td>film</td>
	 * </tr>
	 * <tr>
	 * <td>21</td>
	 * <td>g</td>
	 * <td>g</td>
	 * <td>g</td>
	 * <td>g</td>
	 * <td>g</td>
	 * <td>g</td>
	 * </tr>
	 * <tr>
	 * <td>22</td>
	 * <td>gauze</td>
	 * <td>gauze</td>
	 * <td>gauze</td>
	 * <td>compresse</td>
	 * <td>compres</td>
	 * <td>compresse</td>
	 * </tr>
	 * <tr>
	 * <td>23</td>
	 * <td>gran.</td>
	 * <td>gran</td>
	 * <td>granule</td>
	 * <td>gran.</td>
	 * <td>gran.</td>
	 * <td>granule</td>
	 * </tr>
	 * <tr>
	 * <td>24</td>
	 * <td>gum</td>
	 * <td>gum</td>
	 * <td>gum</td>
	 * <td>gom.</td>
	 * <td>gom.</td>
	 * <td>gomme</td>
	 * </tr>
	 * <tr>
	 * <td>25</td>
	 * <td>gumm.</td>
	 * <td>gumm.</td>
	 * <td>gummy</td>
	 * <td>gum.</td>
	 * <td>gum.</td>
	 * <td>gummie</td>
	 * </tr>
	 * <tr>
	 * <td>26</td>
	 * <td>implant</td>
	 * <td>implant</td>
	 * <td>implant</td>
	 * <td>implant</td>
	 * <td>implant</td>
	 * <td>implant</td>
	 * </tr>
	 * <tr>
	 * <td>27</td>
	 * <td>ingred or liq</td>
	 * <td>ingred or liq</td>
	 * <td>ingred or liq</td>
	 * <td>Ingr&Atilde;&copy;d ou liq</td>
	 * <td>Ingr&Atilde;&copy;d ou liq</td>
	 * <td>Ingr&Atilde;&copy;d ou liq</td>
	 * </tr>
	 * <tr>
	 * <td>28</td>
	 * <td>inh.</td>
	 * <td>inh.</td>
	 * <td>inhaler</td>
	 * <td>inh.</td>
	 * <td>inh.</td>
	 * <td>inhalateur</td>
	 * </tr>
	 * <tr>
	 * <td>29</td>
	 * <td>IU</td>
	 * <td>IU</td>
	 * <td>IU</td>
	 * <td>UI</td>
	 * <td>UI</td>
	 * <td>UI</td>
	 * </tr>
	 * <tr>
	 * <td>30</td>
	 * <td>IUD</td>
	 * <td>IUD</td>
	 * <td>IUD</td>
	 * <td>st&Atilde;&copy;rilet</td>
	 * <td>st&Atilde;&copy;rilet</td>
	 * <td>st&Atilde;&copy;rilet</td>
	 * </tr>
	 * <tr>
	 * <td>31</td>
	 * <td>kit</td>
	 * <td>kit</td>
	 * <td>kit</td>
	 * <td>ens.</td>
	 * <td>ens.</td>
	 * <td>ensemble</td>
	 * </tr>
	 * <tr>
	 * <td>32</td>
	 * <td>L</td>
	 * <td>L</td>
	 * <td>liter</td>
	 * <td>L</td>
	 * <td>L</td>
	 * <td>litre</td>
	 * </tr>
	 * <tr>
	 * <td>33</td>
	 * <td>lancet</td>
	 * <td>lancet</td>
	 * <td>lancet</td>
	 * <td>lancette</td>
	 * <td>lancet.</td>
	 * <td>lancette</td>
	 * </tr>
	 * <tr>
	 * <td>34</td>
	 * <td>loz.</td>
	 * <td>loz.</td>
	 * <td>lozenge</td>
	 * <td>pastille</td>
	 * <td>past.</td>
	 * <td>pastille</td>
	 * </tr>
	 * <tr>
	 * <td>35</td>
	 * <td>m.u.</td>
	 * <td>m.u.</td>
	 * <td>million units</td>
	 * <td>m.u.</td>
	 * <td>m.u.</td>
	 * <td>million d'unit&Atilde;&copy;s</td>
	 * </tr>
	 * <tr>
	 * <td>36</td>
	 * <td>mask</td>
	 * <td>mask</td>
	 * <td>mask</td>
	 * <td>masque</td>
	 * <td>masque</td>
	 * <td>masque</td>
	 * </tr>
	 * <tr>
	 * <td>37</td>
	 * <td>meter</td>
	 * <td>meter</td>
	 * <td>meter</td>
	 * <td>lecteur</td>
	 * <td>lecteur</td>
	 * <td>lecteur</td>
	 * </tr>
	 * <tr>
	 * <td>38</td>
	 * <td>mg</td>
	 * <td>mg</td>
	 * <td>mg</td>
	 * <td>mg</td>
	 * <td>mg</td>
	 * <td>mg</td>
	 * </tr>
	 * <tr>
	 * <td>39</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * </tr>
	 * <tr>
	 * <td>40</td>
	 * <td>nebule</td>
	 * <td>nebule</td>
	 * <td>nebule</td>
	 * <td>n&Atilde;&copy;bule</td>
	 * <td>n&Atilde;&copy;bule</td>
	 * <td>n&Atilde;&copy;bule</td>
	 * </tr>
	 * <tr>
	 * <td>41</td>
	 * <td>needle</td>
	 * <td>needle</td>
	 * <td>needle</td>
	 * <td>aiguille</td>
	 * <td>aiguille</td>
	 * <td>aiguille</td>
	 * </tr>
	 * <tr>
	 * <td>42</td>
	 * <td>pack</td>
	 * <td>pack</td>
	 * <td>pack</td>
	 * <td>trousse</td>
	 * <td>trousse</td>
	 * <td>trousse</td>
	 * </tr>
	 * <tr>
	 * <td>43</td>
	 * <td>pack.</td>
	 * <td>pack.</td>
	 * <td>packet</td>
	 * <td>paqt</td>
	 * <td>paqt</td>
	 * <td>paquet</td>
	 * </tr>
	 * <tr>
	 * <td>44</td>
	 * <td>pad</td>
	 * <td>pad</td>
	 * <td>pad</td>
	 * <td>pad</td>
	 * <td>pad</td>
	 * <td>pad</td>
	 * </tr>
	 * <tr>
	 * <td>45</td>
	 * <td>patch</td>
	 * <td>patch</td>
	 * <td>patch</td>
	 * <td>timbre</td>
	 * <td>tbr</td>
	 * <td>timbre</td>
	 * </tr>
	 * <tr>
	 * <td>46</td>
	 * <td>pen</td>
	 * <td>pen</td>
	 * <td>pen</td>
	 * <td>stylo</td>
	 * <td>stylo</td>
	 * <td>stylo</td>
	 * </tr>
	 * <tr>
	 * <td>47</td>
	 * <td>piece</td>
	 * <td>piece</td>
	 * <td>piece</td>
	 * <td>morceau</td>
	 * <td>morc.</td>
	 * <td>morceau</td>
	 * </tr>
	 * <tr>
	 * <td>48</td>
	 * <td>pom.</td>
	 * <td>pom.</td>
	 * <td>pomade</td>
	 * <td>pom.</td>
	 * <td>pom.</td>
	 * <td>pomade</td>
	 * </tr>
	 * <tr>
	 * <td>49</td>
	 * <td>pouch</td>
	 * <td>pouch</td>
	 * <td>pouch</td>
	 * <td>sachet</td>
	 * <td>sachet</td>
	 * <td>sachet</td>
	 * </tr>
	 * <tr>
	 * <td>50</td>
	 * <td>prep.</td>
	 * <td>prep.</td>
	 * <td>prep.</td>
	 * <td>+prep.</td>
	 * <td>+prep.</td>
	 * <td>+prep.</td>
	 * </tr>
	 * <tr>
	 * <td>51</td>
	 * <td>pudding</td>
	 * <td>pding</td>
	 * <td>pudding</td>
	 * <td>pouding</td>
	 * <td>pding</td>
	 * <td>pouding</td>
	 * </tr>
	 * <tr>
	 * <td>52</td>
	 * <td>ring</td>
	 * <td>ring</td>
	 * <td>ring</td>
	 * <td>anneau</td>
	 * <td>anneau</td>
	 * <td>anneau</td>
	 * </tr>
	 * <tr>
	 * <td>53</td>
	 * <td>soap</td>
	 * <td>soap</td>
	 * <td>soap</td>
	 * <td>savon</td>
	 * <td>savon</td>
	 * <td>savon</td>
	 * </tr>
	 * <tr>
	 * <td>54</td>
	 * <td>sponge</td>
	 * <td>sponge</td>
	 * <td>sponge</td>
	 * <td>&Atilde;&copy;ponge</td>
	 * <td>eponge</td>
	 * <td>&Atilde;&copy;ponge</td>
	 * </tr>
	 * <tr>
	 * <td>55</td>
	 * <td>spray</td>
	 * <td>spray</td>
	 * <td>spray</td>
	 * <td>vapo.</td>
	 * <td>vapo.</td>
	 * <td>vaporisateur</td>
	 * </tr>
	 * <tr>
	 * <td>56</td>
	 * <td>stick</td>
	 * <td>stick</td>
	 * <td>stick</td>
	 * <td>b&Atilde;&cent;ton</td>
	 * <td>b&Atilde;&cent;ton</td>
	 * <td>b&Atilde;&cent;ton</td>
	 * </tr>
	 * <tr>
	 * <td>57</td>
	 * <td>strip</td>
	 * <td>strip</td>
	 * <td>strip</td>
	 * <td>bande</td>
	 * <td>bande</td>
	 * <td>bande</td>
	 * </tr>
	 * <tr>
	 * <td>58</td>
	 * <td>supp.</td>
	 * <td>supp.</td>
	 * <td>suppository</td>
	 * <td>supp.</td>
	 * <td>supp.</td>
	 * <td>suppositoire</td>
	 * </tr>
	 * <tr>
	 * <td>59</td>
	 * <td>swab</td>
	 * <td>swab</td>
	 * <td>swab</td>
	 * <td>tige</td>
	 * <td>tige</td>
	 * <td>tige</td>
	 * </tr>
	 * <tr>
	 * <td>60</td>
	 * <td>syr.</td>
	 * <td>syr.</td>
	 * <td>syringe</td>
	 * <td>ser.</td>
	 * <td>ser.</td>
	 * <td>seringue</td>
	 * </tr>
	 * <tr>
	 * <td>61</td>
	 * <td>t.strip</td>
	 * <td>t.strip</td>
	 * <td>test strip</td>
	 * <td>bandlt</td>
	 * <td>bandlet</td>
	 * <td>bandelette</td>
	 * </tr>
	 * <tr>
	 * <td>62</td>
	 * <td>tab.</td>
	 * <td>tab.</td>
	 * <td>tablet</td>
	 * <td>co.</td>
	 * <td>comp.</td>
	 * <td>comprim&Atilde;&copy;</td>
	 * </tr>
	 * <tr>
	 * <td>63</td>
	 * <td>tampon</td>
	 * <td>tampon</td>
	 * <td>tampon</td>
	 * <td>tampon</td>
	 * <td>tampon</td>
	 * <td>tampon</td>
	 * </tr>
	 * <tr>
	 * <td>64</td>
	 * <td>tape</td>
	 * <td>tape</td>
	 * <td>tape</td>
	 * <td>ruban</td>
	 * <td>ruban</td>
	 * <td>ruban</td>
	 * </tr>
	 * <tr>
	 * <td>65</td>
	 * <td>tbls.</td>
	 * <td>tbls.</td>
	 * <td>tablespoon</td>
	 * <td>c.&Atilde;&nbsp; s.</td>
	 * <td>c.&Atilde;&nbsp; s.</td>
	 * <td>cuiller.&Atilde;&nbsp; soupe</td>
	 * </tr>
	 * <tr>
	 * <td>66</td>
	 * <td>teas.</td>
	 * <td>teas.</td>
	 * <td>teaspoon</td>
	 * <td>c.&Atilde;&nbsp; t.</td>
	 * <td>c.&Atilde;&nbsp; t.</td>
	 * <td>cuiller.&Atilde;&nbsp; th&Atilde;&copy;</td>
	 * </tr>
	 * <tr>
	 * <td>67</td>
	 * <td>test</td>
	 * <td>test</td>
	 * <td>test</td>
	 * <td>test</td>
	 * <td>test</td>
	 * <td>test</td>
	 * </tr>
	 * <tr>
	 * <td>68</td>
	 * <td>tincture</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * <td>teinture</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * </tr>
	 * <tr>
	 * <td>69</td>
	 * <td>tongue</td>
	 * <td>tongue</td>
	 * <td>tongue</td>
	 * <td>languette</td>
	 * <td>languet.</td>
	 * <td>languette</td>
	 * </tr>
	 * <tr>
	 * <td>70</td>
	 * <td>tube</td>
	 * <td>tube</td>
	 * <td>tube</td>
	 * <td>tube</td>
	 * <td>tube</td>
	 * <td>tube</td>
	 * </tr>
	 * <tr>
	 * <td>71</td>
	 * <td>tulle</td>
	 * <td>tulle</td>
	 * <td>tulle</td>
	 * <td>tulle</td>
	 * <td>tulle</td>
	 * <td>tulle</td>
	 * </tr>
	 * <tr>
	 * <td>72</td>
	 * <td>u.</td>
	 * <td>u.</td>
	 * <td>unit</td>
	 * <td>u.</td>
	 * <td>u.</td>
	 * <td>unit&Atilde;&copy;</td>
	 * </tr>
	 * <tr>
	 * <td>73</td>
	 * <td>v.supp.</td>
	 * <td>v.supp.</td>
	 * <td>vag.supp.</td>
	 * <td>ovule</td>
	 * <td>ovule</td>
	 * <td>ovule</td>
	 * </tr>
	 * <tr>
	 * <td>74</td>
	 * <td>vial</td>
	 * <td>vial</td>
	 * <td>vial</td>
	 * <td>fiole</td>
	 * <td>fiole</td>
	 * <td>fiole</td>
	 * </tr>
	 * <tr>
	 * <td>75</td>
	 * <td>wfr</td>
	 * <td>wfr</td>
	 * <td>wafer</td>
	 * <td>wfr</td>
	 * <td>wfr</td>
	 * <td>wafer</td>
	 * </tr>
	 * <tr>
	 * <td>76</td>
	 * <td>wick</td>
	 * <td>wick</td>
	 * <td>wick</td>
	 * <td>m&Atilde;&uml;che</td>
	 * <td>m&Atilde;&uml;che</td>
	 * <td>m&Atilde;&uml;che</td>
	 * </tr>
	 * <tr>
	 * <td>77</td>
	 * <td>zzz</td>
	 * <td>zzz</td>
	 * <td>zzz</td>
	 * <td>zzz</td>
	 * <td>zzz</td>
	 * <td>zzz</td>
	 * </tr>
	 * <tr>
	 * <td>78</td>
	 * <td>oz</td>
	 * <td>floz</td>
	 * <td>fluid ounce</td>
	 * <td>oz liq</td>
	 * <td>oz liq</td>
	 * <td>once liquide</td>
	 * </tr>
	 * <tr>
	 * <td>79</td>
	 * <td>pint</td>
	 * <td>pint</td>
	 * <td>pint</td>
	 * <td>pinte</td>
	 * <td>pinte</td>
	 * <td>pinte</td>
	 * </tr>
	 * <tr>
	 * <td>80</td>
	 * <td>n/s</td>
	 * <td>n/s</td>
	 * <td>not specified</td>
	 * <td>n/s</td>
	 * <td>n/s</td>
	 * <td>not specified</td>
	 * </tr>
	 * <tr>
	 * <td>81</td>
	 * <td>m/d</td>
	 * <td>m/d</td>
	 * <td>mutually defined</td>
	 * <td>m/d</td>
	 * <td>m/d</td>
	 * <td>mutually defined</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * @see org.oscarehr.oscarRx.erx.model.ERxPrescription.qtyUnitToString
	 */
	private int qtyUnitId;
	/**
	 * A (localized) string representing the
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.qtyUnitId}
	 * interpretation.
	 * 
	 * This field refers to both
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.quantity} and
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.narcoticTotalQuantity}
	 * fields.
	 * 
	 * This field is a fallback in case the intepreting software is missing the
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.qtyUnitId}.
	 */
	private String qtyUnitToString;
	/**
	 * The number of repeats.
	 */
	private int refills;
	/**
	 * Refills can be specified as a duration, which defines how long the refill
	 * is expected to last.
	 * 
	 * This field combines with
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.refillsDurationTimeUnit}
	 * .
	 */
	private int refillsDuration;
	/**
	 * This field combines with
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.refills}.
	 */
	private PrescriptionTimeUnit refillsDurationTimeUnit;
	/**
	 * Contains the prescription's sig (i.e.: instructions on how to take the
	 * drug) if manually entered by the physician.
	 * 
	 * Example: "1 cap. daily PRN HS" means:
	 * 
	 * - 1 = sig dosage (i.e.:
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigDosage})
	 * 
	 * - cap. = sig unit (i.e.:
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigUnitId} and
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigUnitToString})
	 * 
	 * - daily = sig frequency (i.e.:
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigFrequencyId} and
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigFrequencyToString}
	 * )
	 * 
	 * - HS = sig specifier (i.e.:
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigSpecifierId} and
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigSpecifierToString}
	 * )
	 */
	private String sigManual;
	/**
	 * Represents the quantity of drug to be administrered in the unit
	 * represented by
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigUnitId}.
	 */
	private float sigDosage;
	/**
	 * Represents the unit of measure of
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigDosage}.
	 * 
	 * This field appears to refer to
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigDosage}.
	 * 
	 * A table of the possible values for this field and their corresponding
	 * interpretations are as follows:
	 * <table>
	 * <caption><b>Units of measure</b></caption> <thead>
	 * <tr>
	 * <th>UnitOfMeasureId</th>
	 * <th>SIG Unit (en)</th>
	 * <th>Qty Unit (en)</th>
	 * <th>Long Name (en)</th>
	 * <th>SIG Unit (fr)</th>
	 * <th>Qty Unit (fr)</th>
	 * <th>Long Name (fr)</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>1</td>
	 * <td>adapt.</td>
	 * <td>adapt.</td>
	 * <td>adapter</td>
	 * <td>embout</td>
	 * <td>embout</td>
	 * <td>embout</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>advice</td>
	 * <td>advice</td>
	 * <td>advice</td>
	 * <td>Opinion</td>
	 * <td>Opinion</td>
	 * <td>Opinion</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>app.</td>
	 * <td>app.</td>
	 * <td>app.</td>
	 * <td>app.</td>
	 * <td>app.</td>
	 * <td>app.</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>appl.</td>
	 * <td>appl.</td>
	 * <td>application</td>
	 * <td>appl.</td>
	 * <td>appl.</td>
	 * <td>application</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>bag</td>
	 * <td>bag</td>
	 * <td>bag</td>
	 * <td>sac</td>
	 * <td>sac</td>
	 * <td>sac</td>
	 * </tr>
	 * <tr>
	 * <td>6</td>
	 * <td>band.</td>
	 * <td>band.</td>
	 * <td>bandage</td>
	 * <td>band.</td>
	 * <td>band.</td>
	 * <td>bandage</td>
	 * </tr>
	 * <tr>
	 * <td>7</td>
	 * <td>bar</td>
	 * <td>bar</td>
	 * <td>bar</td>
	 * <td>barre</td>
	 * <td>barre</td>
	 * <td>barre</td>
	 * </tr>
	 * <tr>
	 * <td>8</td>
	 * <td>batt.</td>
	 * <td>batt.</td>
	 * <td>batterie</td>
	 * <td>pile</td>
	 * <td>pile</td>
	 * <td>pile</td>
	 * </tr>
	 * <tr>
	 * <td>9</td>
	 * <td>botl</td>
	 * <td>botl</td>
	 * <td>bottle</td>
	 * <td>bout.</td>
	 * <td>bout.</td>
	 * <td>bouteille</td>
	 * </tr>
	 * <tr>
	 * <td>10</td>
	 * <td>box</td>
	 * <td>box</td>
	 * <td>box</td>
	 * <td>bo&Atilde;&reg;te</td>
	 * <td>bo&Atilde;&reg;te</td>
	 * <td>bo&Atilde;&reg;te</td>
	 * </tr>
	 * <tr>
	 * <td>11</td>
	 * <td>cap.</td>
	 * <td>cap.</td>
	 * <td>caplet</td>
	 * <td>capl.</td>
	 * <td>capl.</td>
	 * <td>caplet</td>
	 * </tr>
	 * <tr>
	 * <td>12</td>
	 * <td>caps.</td>
	 * <td>caps.</td>
	 * <td>capsule</td>
	 * <td>caps.</td>
	 * <td>caps.</td>
	 * <td>capsule</td>
	 * </tr>
	 * <tr>
	 * <td>13</td>
	 * <td>cart.</td>
	 * <td>cart.</td>
	 * <td>cartridge</td>
	 * <td>cart.</td>
	 * <td>cart.</td>
	 * <td>cartouche</td>
	 * </tr>
	 * <tr>
	 * <td>14</td>
	 * <td>cont.</td>
	 * <td>cont.</td>
	 * <td>container</td>
	 * <td>cont.</td>
	 * <td>cont.</td>
	 * <td>contenant</td>
	 * </tr>
	 * <tr>
	 * <td>15</td>
	 * <td>dev.</td>
	 * <td>dev.</td>
	 * <td>device</td>
	 * <td>disp.</td>
	 * <td>disp.</td>
	 * <td>dispositif</td>
	 * </tr>
	 * <tr>
	 * <td>16</td>
	 * <td>dose</td>
	 * <td>dose</td>
	 * <td>dose</td>
	 * <td>dose</td>
	 * <td>dose</td>
	 * <td>dose</td>
	 * </tr>
	 * <tr>
	 * <td>17</td>
	 * <td>dres.</td>
	 * <td>dres.</td>
	 * <td>dressing</td>
	 * <td>pans.</td>
	 * <td>pans.</td>
	 * <td>pansement</td>
	 * </tr>
	 * <tr>
	 * <td>18</td>
	 * <td>drop</td>
	 * <td>drop</td>
	 * <td>drop</td>
	 * <td>gte</td>
	 * <td>gte</td>
	 * <td>goutte</td>
	 * </tr>
	 * <tr>
	 * <td>19</td>
	 * <td>ea.</td>
	 * <td>ea.</td>
	 * <td>each</td>
	 * <td>chac.</td>
	 * <td>chac.</td>
	 * <td>chacun</td>
	 * </tr>
	 * <tr>
	 * <td>20</td>
	 * <td>film</td>
	 * <td>film</td>
	 * <td>film</td>
	 * <td>film</td>
	 * <td>film</td>
	 * <td>film</td>
	 * </tr>
	 * <tr>
	 * <td>21</td>
	 * <td>g</td>
	 * <td>g</td>
	 * <td>g</td>
	 * <td>g</td>
	 * <td>g</td>
	 * <td>g</td>
	 * </tr>
	 * <tr>
	 * <td>22</td>
	 * <td>gauze</td>
	 * <td>gauze</td>
	 * <td>gauze</td>
	 * <td>compresse</td>
	 * <td>compres</td>
	 * <td>compresse</td>
	 * </tr>
	 * <tr>
	 * <td>23</td>
	 * <td>gran.</td>
	 * <td>gran</td>
	 * <td>granule</td>
	 * <td>gran.</td>
	 * <td>gran.</td>
	 * <td>granule</td>
	 * </tr>
	 * <tr>
	 * <td>24</td>
	 * <td>gum</td>
	 * <td>gum</td>
	 * <td>gum</td>
	 * <td>gom.</td>
	 * <td>gom.</td>
	 * <td>gomme</td>
	 * </tr>
	 * <tr>
	 * <td>25</td>
	 * <td>gumm.</td>
	 * <td>gumm.</td>
	 * <td>gummy</td>
	 * <td>gum.</td>
	 * <td>gum.</td>
	 * <td>gummie</td>
	 * </tr>
	 * <tr>
	 * <td>26</td>
	 * <td>implant</td>
	 * <td>implant</td>
	 * <td>implant</td>
	 * <td>implant</td>
	 * <td>implant</td>
	 * <td>implant</td>
	 * </tr>
	 * <tr>
	 * <td>27</td>
	 * <td>ingred or liq</td>
	 * <td>ingred or liq</td>
	 * <td>ingred or liq</td>
	 * <td>Ingr&Atilde;&copy;d ou liq</td>
	 * <td>Ingr&Atilde;&copy;d ou liq</td>
	 * <td>Ingr&Atilde;&copy;d ou liq</td>
	 * </tr>
	 * <tr>
	 * <td>28</td>
	 * <td>inh.</td>
	 * <td>inh.</td>
	 * <td>inhaler</td>
	 * <td>inh.</td>
	 * <td>inh.</td>
	 * <td>inhalateur</td>
	 * </tr>
	 * <tr>
	 * <td>29</td>
	 * <td>IU</td>
	 * <td>IU</td>
	 * <td>IU</td>
	 * <td>UI</td>
	 * <td>UI</td>
	 * <td>UI</td>
	 * </tr>
	 * <tr>
	 * <td>30</td>
	 * <td>IUD</td>
	 * <td>IUD</td>
	 * <td>IUD</td>
	 * <td>st&Atilde;&copy;rilet</td>
	 * <td>st&Atilde;&copy;rilet</td>
	 * <td>st&Atilde;&copy;rilet</td>
	 * </tr>
	 * <tr>
	 * <td>31</td>
	 * <td>kit</td>
	 * <td>kit</td>
	 * <td>kit</td>
	 * <td>ens.</td>
	 * <td>ens.</td>
	 * <td>ensemble</td>
	 * </tr>
	 * <tr>
	 * <td>32</td>
	 * <td>L</td>
	 * <td>L</td>
	 * <td>liter</td>
	 * <td>L</td>
	 * <td>L</td>
	 * <td>litre</td>
	 * </tr>
	 * <tr>
	 * <td>33</td>
	 * <td>lancet</td>
	 * <td>lancet</td>
	 * <td>lancet</td>
	 * <td>lancette</td>
	 * <td>lancet.</td>
	 * <td>lancette</td>
	 * </tr>
	 * <tr>
	 * <td>34</td>
	 * <td>loz.</td>
	 * <td>loz.</td>
	 * <td>lozenge</td>
	 * <td>pastille</td>
	 * <td>past.</td>
	 * <td>pastille</td>
	 * </tr>
	 * <tr>
	 * <td>35</td>
	 * <td>m.u.</td>
	 * <td>m.u.</td>
	 * <td>million units</td>
	 * <td>m.u.</td>
	 * <td>m.u.</td>
	 * <td>million d'unit&Atilde;&copy;s</td>
	 * </tr>
	 * <tr>
	 * <td>36</td>
	 * <td>mask</td>
	 * <td>mask</td>
	 * <td>mask</td>
	 * <td>masque</td>
	 * <td>masque</td>
	 * <td>masque</td>
	 * </tr>
	 * <tr>
	 * <td>37</td>
	 * <td>meter</td>
	 * <td>meter</td>
	 * <td>meter</td>
	 * <td>lecteur</td>
	 * <td>lecteur</td>
	 * <td>lecteur</td>
	 * </tr>
	 * <tr>
	 * <td>38</td>
	 * <td>mg</td>
	 * <td>mg</td>
	 * <td>mg</td>
	 * <td>mg</td>
	 * <td>mg</td>
	 * <td>mg</td>
	 * </tr>
	 * <tr>
	 * <td>39</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * </tr>
	 * <tr>
	 * <td>40</td>
	 * <td>nebule</td>
	 * <td>nebule</td>
	 * <td>nebule</td>
	 * <td>n&Atilde;&copy;bule</td>
	 * <td>n&Atilde;&copy;bule</td>
	 * <td>n&Atilde;&copy;bule</td>
	 * </tr>
	 * <tr>
	 * <td>41</td>
	 * <td>needle</td>
	 * <td>needle</td>
	 * <td>needle</td>
	 * <td>aiguille</td>
	 * <td>aiguille</td>
	 * <td>aiguille</td>
	 * </tr>
	 * <tr>
	 * <td>42</td>
	 * <td>pack</td>
	 * <td>pack</td>
	 * <td>pack</td>
	 * <td>trousse</td>
	 * <td>trousse</td>
	 * <td>trousse</td>
	 * </tr>
	 * <tr>
	 * <td>43</td>
	 * <td>pack.</td>
	 * <td>pack.</td>
	 * <td>packet</td>
	 * <td>paqt</td>
	 * <td>paqt</td>
	 * <td>paquet</td>
	 * </tr>
	 * <tr>
	 * <td>44</td>
	 * <td>pad</td>
	 * <td>pad</td>
	 * <td>pad</td>
	 * <td>pad</td>
	 * <td>pad</td>
	 * <td>pad</td>
	 * </tr>
	 * <tr>
	 * <td>45</td>
	 * <td>patch</td>
	 * <td>patch</td>
	 * <td>patch</td>
	 * <td>timbre</td>
	 * <td>tbr</td>
	 * <td>timbre</td>
	 * </tr>
	 * <tr>
	 * <td>46</td>
	 * <td>pen</td>
	 * <td>pen</td>
	 * <td>pen</td>
	 * <td>stylo</td>
	 * <td>stylo</td>
	 * <td>stylo</td>
	 * </tr>
	 * <tr>
	 * <td>47</td>
	 * <td>piece</td>
	 * <td>piece</td>
	 * <td>piece</td>
	 * <td>morceau</td>
	 * <td>morc.</td>
	 * <td>morceau</td>
	 * </tr>
	 * <tr>
	 * <td>48</td>
	 * <td>pom.</td>
	 * <td>pom.</td>
	 * <td>pomade</td>
	 * <td>pom.</td>
	 * <td>pom.</td>
	 * <td>pomade</td>
	 * </tr>
	 * <tr>
	 * <td>49</td>
	 * <td>pouch</td>
	 * <td>pouch</td>
	 * <td>pouch</td>
	 * <td>sachet</td>
	 * <td>sachet</td>
	 * <td>sachet</td>
	 * </tr>
	 * <tr>
	 * <td>50</td>
	 * <td>prep.</td>
	 * <td>prep.</td>
	 * <td>prep.</td>
	 * <td>+prep.</td>
	 * <td>+prep.</td>
	 * <td>+prep.</td>
	 * </tr>
	 * <tr>
	 * <td>51</td>
	 * <td>pudding</td>
	 * <td>pding</td>
	 * <td>pudding</td>
	 * <td>pouding</td>
	 * <td>pding</td>
	 * <td>pouding</td>
	 * </tr>
	 * <tr>
	 * <td>52</td>
	 * <td>ring</td>
	 * <td>ring</td>
	 * <td>ring</td>
	 * <td>anneau</td>
	 * <td>anneau</td>
	 * <td>anneau</td>
	 * </tr>
	 * <tr>
	 * <td>53</td>
	 * <td>soap</td>
	 * <td>soap</td>
	 * <td>soap</td>
	 * <td>savon</td>
	 * <td>savon</td>
	 * <td>savon</td>
	 * </tr>
	 * <tr>
	 * <td>54</td>
	 * <td>sponge</td>
	 * <td>sponge</td>
	 * <td>sponge</td>
	 * <td>&Atilde;&copy;ponge</td>
	 * <td>eponge</td>
	 * <td>&Atilde;&copy;ponge</td>
	 * </tr>
	 * <tr>
	 * <td>55</td>
	 * <td>spray</td>
	 * <td>spray</td>
	 * <td>spray</td>
	 * <td>vapo.</td>
	 * <td>vapo.</td>
	 * <td>vaporisateur</td>
	 * </tr>
	 * <tr>
	 * <td>56</td>
	 * <td>stick</td>
	 * <td>stick</td>
	 * <td>stick</td>
	 * <td>b&Atilde;&cent;ton</td>
	 * <td>b&Atilde;&cent;ton</td>
	 * <td>b&Atilde;&cent;ton</td>
	 * </tr>
	 * <tr>
	 * <td>57</td>
	 * <td>strip</td>
	 * <td>strip</td>
	 * <td>strip</td>
	 * <td>bande</td>
	 * <td>bande</td>
	 * <td>bande</td>
	 * </tr>
	 * <tr>
	 * <td>58</td>
	 * <td>supp.</td>
	 * <td>supp.</td>
	 * <td>suppository</td>
	 * <td>supp.</td>
	 * <td>supp.</td>
	 * <td>suppositoire</td>
	 * </tr>
	 * <tr>
	 * <td>59</td>
	 * <td>swab</td>
	 * <td>swab</td>
	 * <td>swab</td>
	 * <td>tige</td>
	 * <td>tige</td>
	 * <td>tige</td>
	 * </tr>
	 * <tr>
	 * <td>60</td>
	 * <td>syr.</td>
	 * <td>syr.</td>
	 * <td>syringe</td>
	 * <td>ser.</td>
	 * <td>ser.</td>
	 * <td>seringue</td>
	 * </tr>
	 * <tr>
	 * <td>61</td>
	 * <td>t.strip</td>
	 * <td>t.strip</td>
	 * <td>test strip</td>
	 * <td>bandlt</td>
	 * <td>bandlet</td>
	 * <td>bandelette</td>
	 * </tr>
	 * <tr>
	 * <td>62</td>
	 * <td>tab.</td>
	 * <td>tab.</td>
	 * <td>tablet</td>
	 * <td>co.</td>
	 * <td>comp.</td>
	 * <td>comprim&Atilde;&copy;</td>
	 * </tr>
	 * <tr>
	 * <td>63</td>
	 * <td>tampon</td>
	 * <td>tampon</td>
	 * <td>tampon</td>
	 * <td>tampon</td>
	 * <td>tampon</td>
	 * <td>tampon</td>
	 * </tr>
	 * <tr>
	 * <td>64</td>
	 * <td>tape</td>
	 * <td>tape</td>
	 * <td>tape</td>
	 * <td>ruban</td>
	 * <td>ruban</td>
	 * <td>ruban</td>
	 * </tr>
	 * <tr>
	 * <td>65</td>
	 * <td>tbls.</td>
	 * <td>tbls.</td>
	 * <td>tablespoon</td>
	 * <td>c.&Atilde;&nbsp; s.</td>
	 * <td>c.&Atilde;&nbsp; s.</td>
	 * <td>cuiller.&Atilde;&nbsp; soupe</td>
	 * </tr>
	 * <tr>
	 * <td>66</td>
	 * <td>teas.</td>
	 * <td>teas.</td>
	 * <td>teaspoon</td>
	 * <td>c.&Atilde;&nbsp; t.</td>
	 * <td>c.&Atilde;&nbsp; t.</td>
	 * <td>cuiller.&Atilde;&nbsp; th&Atilde;&copy;</td>
	 * </tr>
	 * <tr>
	 * <td>67</td>
	 * <td>test</td>
	 * <td>test</td>
	 * <td>test</td>
	 * <td>test</td>
	 * <td>test</td>
	 * <td>test</td>
	 * </tr>
	 * <tr>
	 * <td>68</td>
	 * <td>tincture</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * <td>teinture</td>
	 * <td>ml</td>
	 * <td>ml</td>
	 * </tr>
	 * <tr>
	 * <td>69</td>
	 * <td>tongue</td>
	 * <td>tongue</td>
	 * <td>tongue</td>
	 * <td>languette</td>
	 * <td>languet.</td>
	 * <td>languette</td>
	 * </tr>
	 * <tr>
	 * <td>70</td>
	 * <td>tube</td>
	 * <td>tube</td>
	 * <td>tube</td>
	 * <td>tube</td>
	 * <td>tube</td>
	 * <td>tube</td>
	 * </tr>
	 * <tr>
	 * <td>71</td>
	 * <td>tulle</td>
	 * <td>tulle</td>
	 * <td>tulle</td>
	 * <td>tulle</td>
	 * <td>tulle</td>
	 * <td>tulle</td>
	 * </tr>
	 * <tr>
	 * <td>72</td>
	 * <td>u.</td>
	 * <td>u.</td>
	 * <td>unit</td>
	 * <td>u.</td>
	 * <td>u.</td>
	 * <td>unit&Atilde;&copy;</td>
	 * </tr>
	 * <tr>
	 * <td>73</td>
	 * <td>v.supp.</td>
	 * <td>v.supp.</td>
	 * <td>vag.supp.</td>
	 * <td>ovule</td>
	 * <td>ovule</td>
	 * <td>ovule</td>
	 * </tr>
	 * <tr>
	 * <td>74</td>
	 * <td>vial</td>
	 * <td>vial</td>
	 * <td>vial</td>
	 * <td>fiole</td>
	 * <td>fiole</td>
	 * <td>fiole</td>
	 * </tr>
	 * <tr>
	 * <td>75</td>
	 * <td>wfr</td>
	 * <td>wfr</td>
	 * <td>wafer</td>
	 * <td>wfr</td>
	 * <td>wfr</td>
	 * <td>wafer</td>
	 * </tr>
	 * <tr>
	 * <td>76</td>
	 * <td>wick</td>
	 * <td>wick</td>
	 * <td>wick</td>
	 * <td>m&Atilde;&uml;che</td>
	 * <td>m&Atilde;&uml;che</td>
	 * <td>m&Atilde;&uml;che</td>
	 * </tr>
	 * <tr>
	 * <td>77</td>
	 * <td>zzz</td>
	 * <td>zzz</td>
	 * <td>zzz</td>
	 * <td>zzz</td>
	 * <td>zzz</td>
	 * <td>zzz</td>
	 * </tr>
	 * <tr>
	 * <td>78</td>
	 * <td>oz</td>
	 * <td>floz</td>
	 * <td>fluid ounce</td>
	 * <td>oz liq</td>
	 * <td>oz liq</td>
	 * <td>once liquide</td>
	 * </tr>
	 * <tr>
	 * <td>79</td>
	 * <td>pint</td>
	 * <td>pint</td>
	 * <td>pint</td>
	 * <td>pinte</td>
	 * <td>pinte</td>
	 * <td>pinte</td>
	 * </tr>
	 * <tr>
	 * <td>80</td>
	 * <td>n/s</td>
	 * <td>n/s</td>
	 * <td>not specified</td>
	 * <td>n/s</td>
	 * <td>n/s</td>
	 * <td>not specified</td>
	 * </tr>
	 * <tr>
	 * <td>81</td>
	 * <td>m/d</td>
	 * <td>m/d</td>
	 * <td>mutually defined</td>
	 * <td>m/d</td>
	 * <td>m/d</td>
	 * <td>mutually defined</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * @see org.oscarehr.oscarRx.erx.model.ERxPrescription.sigUnitToString
	 */
	private int sigUnitId;
	/**
	 * A (localized) string representing the
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigUnitId}
	 * interpretation.
	 * 
	 * This field appears to refer to
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigDosage}.
	 */
	private String sigUnitToString;
	/**
	 * The frequency at which the drug must be administered.
	 * 
	 * A table of the possible values for this field and their corresponding
	 * interpretations are as follows:
	 * <table>
	 * <caption>SigFrequency</caption> <thead>
	 * <tr>
	 * <th>SigFrequencyId</th>
	 * <th>SigFrequencyToString (en-CA)</th>
	 * <th>Description en-CA</th>
	 * <th>SigFrequencyToString (fr-CA)</th>
	 * <th>Description fr-CA</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>1</td>
	 * <td>BID</td>
	 * <td>Twice a day</td>
	 * <td>BID</td>
	 * <td>2 fois par jour</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>TID</td>
	 * <td>Thrice a day</td>
	 * <td>TID</td>
	 * <td>3 fois par jour</td>
	 * </tr>
	 * <tr>
	 * <td>6</td>
	 * <td>QID</td>
	 * <td>4 times a day</td>
	 * <td>QID</td>
	 * <td>4 fois par jour</td>
	 * </tr>
	 * <tr>
	 * <td>7</td>
	 * <td>Q2H</td>
	 * <td>Every 2 hours</td>
	 * <td>Q2H</td>
	 * <td>Aux 2 heures</td>
	 * </tr>
	 * <tr>
	 * <td>8</td>
	 * <td>Q4H</td>
	 * <td>Every 4 hours</td>
	 * <td>Q4H</td>
	 * <td>Aux 4 heures</td>
	 * </tr>
	 * <tr>
	 * <td>9</td>
	 * <td>Q6H</td>
	 * <td>Every 6 hours</td>
	 * <td>Q6H</td>
	 * <td>Aux 6 heures</td>
	 * </tr>
	 * <tr>
	 * <td>10</td>
	 * <td>Q12H</td>
	 * <td>Every 12 hours</td>
	 * <td>Q12H</td>
	 * <td>Aux 12 heures</td>
	 * </tr>
	 * <tr>
	 * <td>11</td>
	 * <td>Q1Week</td>
	 * <td>Once a week</td>
	 * <td>Q1Sem</td>
	 * <td>1 fois par semaine</td>
	 * </tr>
	 * <tr>
	 * <td>12</td>
	 * <td>Daily</td>
	 * <td>Once a day</td>
	 * <td>DIE</td>
	 * <td>1 fois par jour</td>
	 * </tr>
	 * <tr>
	 * <td>13</td>
	 * <td>Q8H</td>
	 * <td>Every 8 hours</td>
	 * <td>Q8H</td>
	 * <td>Aux 8 heures</td>
	 * </tr>
	 * <tr>
	 * <td>14</td>
	 * <td>Q2Days</td>
	 * <td>Every 2 days</td>
	 * <td>Q2Jr</td>
	 * <td>1 fois au deux jours</td>
	 * </tr>
	 * <tr>
	 * <td>15</td>
	 * <td>Q4-6H</td>
	 * <td>every 4 to 6 hours</td>
	 * <td>Q4-6H</td>
	 * <td>aux 4 a 6 heures</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * @see org.oscarehr.oscarRx.erx.model.ERxPrescription.sigFrequencyToString
	 */
	private int sigFrequencyId;
	/**
	 * A (localized) string containing the
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigFrequencyId}
	 * interpretation.
	 * 
	 * This field is a fallback in case the intepreting software is missing the
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigFrequencyId}.
	 */
	private String sigFrequencyToString;
	/**
	 * Additionnal SIG information.
	 * 
	 * For instance, it could specify at which time of day the drug must be
	 * administered or an administration route.
	 * 
	 * A table of the possible values for this field and their corresponding
	 * interpretations are as follows:
	 * <table>
	 * <caption>SigSpecifier</caption> <thead>
	 * <tr>
	 * <th>SigSpecifierId</th>
	 * <th>SIGSpecifierToString (en-CA)</th>
	 * <th>Description en-CA</th>
	 * <th>SIGSpecifierToString (fr-CA)</th>
	 * <th>Description fr-CA</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>1</td>
	 * <td>AC</td>
	 * <td>Before meal</td>
	 * <td>AC</td>
	 * <td>Avant repas</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>PC</td>
	 * <td>After meals</td>
	 * <td>PC</td>
	 * <td>Après repas</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>HS</td>
	 * <td>At bedtime</td>
	 * <td>HS</td>
	 * <td>Au coucher</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>Brkfst</td>
	 * <td>At breakfast</td>
	 * <td>DEJ</td>
	 * <td>Au déjeuner</td>
	 * </tr>
	 * <tr>
	 * <td>6</td>
	 * <td>Lunch</td>
	 * <td>At lunch</td>
	 * <td>DIN</td>
	 * <td>Au dîner</td>
	 * </tr>
	 * <tr>
	 * <td>7</td>
	 * <td>Diner</td>
	 * <td>At diner</td>
	 * <td>SOU</td>
	 * <td>Au souper</td>
	 * </tr>
	 * <tr>
	 * <td>8</td>
	 * <td>Fasting</td>
	 * <td>On an empty stomach</td>
	 * <td>A jeun</td>
	 * <td>A jeun</td>
	 * </tr>
	 * <tr>
	 * <td>9</td>
	 * <td>OU</td>
	 * <td>Both eyes</td>
	 * <td>OU</td>
	 * <td>Les 2 yeux</td>
	 * </tr>
	 * <tr>
	 * <td>10</td>
	 * <td>OS</td>
	 * <td>Left eye</td>
	 * <td>OS</td>
	 * <td>Oeil gauche</td>
	 * </tr>
	 * <tr>
	 * <td>11</td>
	 * <td>OD</td>
	 * <td>Right eye</td>
	 * <td>OD</td>
	 * <td>Oeil droit</td>
	 * </tr>
	 * <tr>
	 * <td>12</td>
	 * <td>Ear - L</td>
	 * <td>Left ear</td>
	 * <td>Oreille G</td>
	 * <td>Oreille gauche</td>
	 * </tr>
	 * <tr>
	 * <td>13</td>
	 * <td>Ear - R</td>
	 * <td>Right ear</td>
	 * <td>Oreille D</td>
	 * <td>Oreille droite</td>
	 * </tr>
	 * <tr>
	 * <td>14</td>
	 * <td>AM</td>
	 * <td>Morning</td>
	 * <td>AM</td>
	 * <td>Avant-midi</td>
	 * </tr>
	 * <tr>
	 * <td>15</td>
	 * <td>PM</td>
	 * <td>Afternoon</td>
	 * <td>PM</td>
	 * <td>Après-midi</td>
	 * </tr>
	 * <tr>
	 * <td>17</td>
	 * <td>With food</td>
	 * <td>With food</td>
	 * <td>En mangeant</td>
	 * <td>En mangeant</td>
	 * </tr>
	 * <tr>
	 * <td>18</td>
	 * <td>Brkfst &amp; Diner</td>
	 * <td>Breakfast and diner</td>
	 * <td>DEJ ET SOU</td>
	 * <td>Déjeuner et souper</td>
	 * </tr>
	 * <tr>
	 * <td>20</td>
	 * <td>AC or PC</td>
	 * <td>1 hr before or 2 hrs after meals</td>
	 * <td>AC ou PC</td>
	 * <td>1 hr avant ou 2 hr apres les repas</td>
	 * </tr>
	 * <tr>
	 * <td>21</td>
	 * <td>Ears - Both</td>
	 * <td>Both ears</td>
	 * <td>Oreilles (2)</td>
	 * <td>dans les oreilles</td>
	 * </tr>
	 * <tr>
	 * <td>22</td>
	 * <td>Nostrils - Both</td>
	 * <td>Nostrils - Both</td>
	 * <td>Narines (2)</td>
	 * <td>Narines (2)</td>
	 * </tr>
	 * <tr>
	 * <td>23</td>
	 * <td>PO</td>
	 * <td>Orally</td>
	 * <td>PO</td>
	 * <td>Oralement</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 */
	private int sigSpecifierId;
	/**
	 * A (localized) string representing the
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigSpecifierId}.
	 * 
	 * This field is a fallback in case the intepreting software is missing the
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigSpecifierId}.
	 */
	private String sigSpecifierToString;
	/**
	 * TRUE if this drug must be administered only if necessary; FALSE
	 * otherwise.
	 */
	private boolean sigPRN;
	/**
	 * The duration of one dispense.
	 * 
	 * This field combines with
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.treatmentDurationTimeUnit}
	 * .
	 */
	private int treatmentDuration;
	/**
	 * This field combines with
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.treatmentDuration}.
	 * 
	 * A table of the possible values for this field and their corresponding
	 * interpretations are as follows:
	 * <table>
	 * <caption>Possible values for TimeUnit fields</caption> <thead>
	 * <tr>
	 * <th>TimeUnit field</th>
	 * <th>Interpretation</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>1</td>
	 * <td>Day</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>Week</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>Month</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>Year</td>
	 * </tr>
	 * <tr>
	 * <td>-1</td>
	 * <td>Empty value</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 */
	private PrescriptionTimeUnit treatmentDurationTimeUnit;
	/**
	 * Manually entered instructions by the physician.
	 */
	private String instructions;
	/**
	 * If the prescription is of type manual, this field contains direct text
	 * input from the physician.
	 * 
	 * The presence of a value in this field implies that:
	 * 
	 * - {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigDosage} = 0
	 * 
	 * - {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigFrequencyId} = 0
	 * 
	 * - {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigSpecifierId} = 0
	 * 
	 * - {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.sigPRN} = FALSE
	 * 
	 * @see <a
	 */
	private String manualPrescriptionContent;
	/**
	 * The time interval at which the
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.narcoticTotalQuantity}
	 * must be served.
	 * 
	 * This field combines with
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.narcoticIntervalTimeUnit}
	 * .
	 * 
	 * @see org.oscarehr.oscarRx.erx.model.ERxPrescription.narcoticTotalQuantity
	 */
	private int narcoticInterval;
	/**
	 * This field combines with
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.narcoticInterval}.
	 * 
	 * A table of the possible values for this field and their corresponding
	 * interpretations are as follows:
	 * <table>
	 * <caption>Possible values for TimeUnit fields</caption> <thead>
	 * <tr>
	 * <th>TimeUnit field</th>
	 * <th>Interpretation</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>1</td>
	 * <td>Day</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>Week</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>Month</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>Year</td>
	 * </tr>
	 * <tr>
	 * <td>-1</td>
	 * <td>Empty value</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 * 
	 * @see org.oscarehr.oscarRx.erx.model.ERxPrescription.narcoticTotalQuantity
	 */
	private PrescriptionTimeUnit narcoticIntervalTimeUnit;
	/**
	 * The total quantity to be served at the specified
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.narcoticInterval}.
	 * 
	 * Example: "# 5g per 2 days Rep.: NR" means:
	 * 
	 * - 5 =
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.narcoticTotalQuantity}
	 * - 2 =
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.narcoticInterval} -
	 * "days" =
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.narcoticIntervalTimeUnit}
	 * 
	 * @see org.oscarehr.oscarRx.erx.model.ERxPrescription.narcoticInterval
	 * @see org.oscarehr.oscarRx.erx.model.ERxPrescription.narcoticIntervalTimeUnit
	 */
	private float narcoticTotalQuantity;
	/**
	 * A (localized) string representing the
	 * {@link org.oscarehr.oscarRx.erx.model.ERxPrescription.statusID}.
	 */
	private String status;
	/**
	 * The current status of this prescription.
	 * 
	 * A table of the possible values for this field and their corresponding
	 * interpretations are as follows:
	 * <table>
	 * <caption><b>Units of measure</b></caption> <thead>
	 * <tr>
	 * <th>StatusId</th>
	 * <th>Interpretation</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>1</td>
	 * <td>Active</td>
	 * </tr>
	 * <tr>
	 * <td>2</td>
	 * <td>Renewed</td>
	 * </tr>
	 * <tr>
	 * <td>3</td>
	 * <td>DefinitivelyCeased</td>
	 * </tr>
	 * <tr>
	 * <td>4</td>
	 * <td>TemporarilyCeased</td>
	 * </tr>
	 * <tr>
	 * <td>5</td>
	 * <td>Canceled</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 */
	private PrescriptionStatusId statusID;
	/**
	 * A (localized) string stating the reason why the prescription was
	 * discontinued/ceased.
	 */
	private String ceasingReason;
	/**
	 * TRUE if the prescription must be dispensed as written (i.e.: if a generic
	 * drug could not substitute the actual prescribed drug); FALSE otherwise.
	 */
	private boolean dispenseAsWritten;
	/**
	 * 1 means the prescribing physician is specifying an increase in dosage;
	 * 
	 * -1 = means the prescribing physician is specifying a decrease in dosage;
	 * 
	 * 0 = means the prescribing physician has not specified any variation
	 */
	private int dosageVariationIndicator;
	/**
	 * An ending date specified by the prescribing physician. Will have the form
	 * "YYYY-MM-DD".
	 */
	private String endingDate;
	/**
	 * If a prescription is modified, this field contains the physician's full
	 * name.
	 */
	private String lastModifiedByName;
	/**
	 * When the prescription is modified, this field contains the last modified
	 * date. Will have the form YYYY-MM-DD
	 */
	private String lastModifiedDate;
	/**
	 * A calculated, estimated ending date for this prescription.
	 */
	private String prescriptionEstimatedEndDate;
	/**
	 * Contains a formattted textual version of the prescription.
	 * 
	 * IF prescription is a Manual Prescription, this field contains a direct
	 * text input from the doctor.
	 * 
	 * IF prescription is NOT a Manual Prescription, this field contains a
	 * (localized) concatenated textual version of all the prescription fields.
	 * 
	 * This field will always be populated.
	 */
	private String formattedPrescriptionToString;
	/**
	 * Contains an unformatted textual version of the prescription.
	 * 
	 * IF prescription is a Manual Prescription, this field contains a direct
	 * text input from the doctor.
	 * 
	 * IF prescription is NOT a Manual Prescription, this field contains a
	 * (localized) concatenated textual version of all the prescription fields.
	 * 
	 * This field will always be populated.
	 */
	private String prescriptionToString;

	/**
	 * Generate a Prescription given a SOAP node.
	 * 
	 * @param toParse
	 *            The SOAP node containing a prescription.
	 */
	public ERxPrescription(Node toParse) throws DOMException {
		super();

		if (!toParse.getNodeName().equals("WSPrescription5")
		    && !toParse.getNodeName().equals("Prescription")) {
			throw new DOMException(
			    DOMException.NOT_SUPPORTED_ERR,
			    "Unable to parse a node of type " + toParse.getNodeName());
		}

		Node child;
		String nodeName;
		String nodeValue;
		NodeList childNodes = toParse.getChildNodes();

		// Loop through the child nodes and parse each one
		for (int i = 0; i < childNodes.getLength(); i++) {
			child = childNodes.item(i);

			// Retrieve information about the child tag we're currently parsing
			nodeName = child.getNodeName();
			nodeValue = child.getTextContent();

			// Don't allow null to be passed in values that we read in
			nodeValue = (nodeValue == null) ? "" : nodeValue;

			// Populate properties in the new object from elements we parse
			if (nodeName.equals("DoctorLicenseNo")) {
				this.doctorLicenseNo = nodeValue;
			}
			else if (nodeName.equals("PatientId")) {
				this.patientId = nodeValue;
			}
			else if (nodeName.equals("PrescriptionId")) {
				this.prescriptionId = nodeValue;
			}
			else if (nodeName.equals("IsDCPrescription")) {
				this.isDCPrescription = Boolean.parseBoolean(nodeValue);
			}
			else if (nodeName.equals("DiscontinuedPrescriptionId")) {
				this.discontinuedPrescriptionId = nodeValue;
			}
			else if (nodeName.equals("DiscontinuedUntil")) {
				this.discontinuedUntil = nodeValue;
			}
			else if (nodeName.equals("IsRenewal")) {
				this.isRenewal = Boolean.parseBoolean(nodeValue);
			}
			else if (nodeName.equals("RenewedPrescriptionId")) {
				this.renewedPrescriptionId = nodeValue;
			}
			else if (nodeName.equals("PrescriptionDateTime")) {
				this.prescriptionDateTime = nodeValue;
			}
			else if (nodeName.equals("ProductName")) {
				this.productName = nodeValue;
			}
			else if (nodeName.equals("ProductForm")) {
				this.productForm = nodeValue;
			}
			else if (nodeName.equals("ProductStrength")) {
				this.productStrength = nodeValue;
			}
			else if (nodeName.equals("DrugCode")) {
				this.drugCode = Long.parseLong(nodeValue);
			}
			else if (nodeName.equals("Qty")) {
				this.quantity = Float.parseFloat(nodeValue);
			}
			else if (nodeName.equals("QtyUnitId")) {
				this.qtyUnitId = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals("QtyUnitToString")) {
				this.qtyUnitToString = nodeValue;
			}
			else if (nodeName.equals("Refills")) {
				this.refills = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals("RefillsDuration")) {
				this.refillsDuration = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals("RefillsDurationTimeUnit")) {
				this.refillsDurationTimeUnit =
				    PrescriptionTimeUnit.parseString(nodeValue);
			}
			else if (nodeName.equals("SigManual")) {
				this.sigManual = nodeValue;
			}
			else if (nodeName.equals("SigDosage")) {
				this.sigDosage = Float.parseFloat(nodeValue);
			}
			else if (nodeName.equals("SigUnitId")) {
				this.sigUnitId = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals("SigUnitToString")) {
				this.sigUnitToString = nodeValue;
			}
			else if (nodeName.equals("SigFrequencyId")) {
				this.sigFrequencyId = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals("SigFrequencyToString")) {
				this.sigFrequencyToString = nodeValue;
			}
			else if (nodeName.equals("SigSpecifierId")) {
				this.sigSpecifierId = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals("SigSpecifierToString")) {
				this.sigSpecifierToString = nodeValue;
			}
			else if (nodeName.equals("SigPRN")) {
				this.sigPRN = Boolean.parseBoolean(nodeValue);
			}
			else if (nodeName.equals("TreatmentDuration")) {
				this.treatmentDuration = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals("TreatmentDurationTimeUnit")) {
				this.treatmentDurationTimeUnit =
				    PrescriptionTimeUnit.parseString(nodeValue);
			}
			else if (nodeName.equals("Instructions")) {
				this.instructions = nodeValue;
			}
			else if (nodeName.equals("ManualPrescriptionContent")) {
				this.manualPrescriptionContent = nodeValue;
			}
			else if (nodeName.equals("NarcoticInterval")) {
				this.narcoticInterval = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals("NarcoticIntervalTimeUnit")) {
				this.narcoticIntervalTimeUnit =
				    PrescriptionTimeUnit.parseString(nodeValue);
			}
			else if (nodeName.equals("NarcoticTotalQuantity")) {
				this.narcoticTotalQuantity = Float.parseFloat(nodeValue);
			}
			else if (nodeName.equals("Status")) {
				this.status = nodeValue;
			}
			else if (nodeName.equals("StatusID")) {
				this.statusID = PrescriptionStatusId.parseString(nodeValue);
			}
			else if (nodeName.equals("CeasingReason")) {
				this.ceasingReason = nodeValue;
			}
			else if (nodeName.equals("DispenseAsWritten")) {
				this.dispenseAsWritten = Boolean.parseBoolean(nodeValue);
			}
			else if (nodeName.equals("DosageVariationIndicator")) {
				this.dosageVariationIndicator = Integer.parseInt(nodeValue);
			}
			else if (nodeName.equals("EndingDate")) {
				this.endingDate = nodeValue;
			}
			else if (nodeName.equals("LastModifiedByName")) {
				this.lastModifiedByName = nodeValue;
			}
			else if (nodeName.equals("LastModifiedDate")) {
				this.lastModifiedDate = nodeValue;
			}
			else if (nodeName.equals("PrescriptionEstimatedEndDate")) {
				this.prescriptionEstimatedEndDate = nodeValue;
			}
			else if (nodeName.equals("FormattedPrescriptionToString")) {
				this.formattedPrescriptionToString = nodeValue;
			}
			else if (nodeName.equals("PrescriptionToString")) {
				this.prescriptionToString = nodeValue;
			}
			else {
				continue;
			}
		}
	}

	/**
	 * Construct a ERxPrescription.
	 * 
	 * @param doctorLicenseNo
	 *            Physician's license number issued by a regulatory body.
	 * @param patientId
	 *            A unique identifier for the patient, as set by the External Prescription software.
	 * @param prescriptionId
	 *            A unique identifier for the prescription.
	 * @param isDCPrescription
	 *            TRUE if this prescription is of type Discontinuation/Ceasing;
	 *            FALSE otherwise.
	 * @param discontinuedPrescriptionId
	 *            References the original
	 *            prescriptionId that is being discontinued/ceased.
	 * @param discontinuedUntil
	 *            Specifies the date that the prescription will be re-activated
	 *            on. If given, will be in the format "YYYY-MM-DD".
	 * @param isRenewal
	 *            TRUE if the prescription is of type Renewal; FALSE otherwise.
	 * @param renewedPrescriptionId
	 *            References the original
	 *            prescriptionId
	 *            that is being renewed.
	 * @param prescriptionDateTime
	 *            Represents the date the prescription was made by the
	 *            physician. If given, will be in the format "YYYY-MM-DD".
	 * @param productName
	 *            The (localized) brand name of the drug that is being
	 *            prescribed.
	 * @param productForm
	 *            The (localized) form in which the prescribed drug will be
	 *            served.
	 * @param productStrength
	 *            The quantity of drug in its form.
	 * @param drugCode
	 *            A identification code for the drug.
	 * @param quantity
	 *            The quantity to dispense.
	 * @param qtyUnitId
	 *            The dispense quantity qualifier.
	 * @param qtyUnitToString
	 *            A (localized) string representing the
	 *            qtyUnitId
	 *            interpretation.
	 * @param refills
	 *            The number of repeats.
	 * @param refillsDuration
	 *            can be specified as a duration, which defines how long the
	 *            refill is expected to last.
	 * @param sigManual
	 *            Contains the prescription's sig (i.e.: instructions on how to
	 *            take the drug) if manually entered by the physician.
	 * @param sigDosage
	 *            Represents the quantity of product in its form.
	 * @param sigUnitId
	 * @param sigUnitToString
	 *            A (localized) string representing the
	 *            sigUnitId
	 *            interpretation.
	 * @param sigFrequencyId
	 *            The frequency at which the drug must be administered.
	 * @param sigFrequencyToString
	 *            A (localized) string containing the
	 *           sigFrequencyId
	 *            interpretation.
	 * @param sigSpecifierId
	 *            Additionnal SIG information.
	 * @param sigSpecifierToString
	 *            A (localized) string representing the
	 *            sigSpecifierId
	 *            .
	 * @param sigPRN
	 *            TRUE if this drug must be administered only if necessary;
	 *            FALSE otherwise.
	 * @param treatmentDuration
	 *            The duration of one dispense.
	 * @param treatmentDurationTimeUnit
	 *            This field combines with
	 *            treatmentDuration
	 *            .
	 * @param instructions
	 *            Manually entered instructions by the physician.
	 * @param manualPrescriptionContent
	 *            If the prescription is of type manual, this field contains
	 *            direct text input from the physician.
	 * @param narcoticInterval
	 *            The time interval at which the
	 *            narcoticTotalQuantity
	 *            must be served.
	 * @param narcoticIntervalTimeUnit
	 *            This field combines with
	 *            narcoticInterval
	 *            .
	 * @param narcoticTotalQuantity
	 *            The total quantity to be served at the specified
	 *            narcoticInterval
	 *            .
	 * @param status
	 *            A (localized) string representing the
	 *            statusID.
	 * @param statusID
	 *            The current status of this prescription.
	 * @param ceasingReason
	 *            A (localized) string stating the reason why the prescription
	 *            was discontinued/ceased.
	 * @param dispenseAsWritten
	 *            TRUE if the prescription must be dispensed as written (i.e.:
	 *            if a generic drug could not substitute the actual prescribed
	 *            drug); FALSE otherwise.
	 * @param dosageVariationIndicator
	 *            1 means the prescribing physician is specifying an increase in
	 *            dosage;
	 * 
	 *            -1 = means the prescribing physician is specifying a decrease
	 *            in dosage;
	 * 
	 *            0 = means the prescribing physician has not specified any
	 *            variation
	 * 
	 * @param endingDate
	 *            An ending date specified by the prescribing physician. Will
	 *            have the form "YYYY-MM-DD".
	 * @param lastModifiedByName
	 *            If a prescription is modified, this field contains the
	 *            physician's full name.
	 * @param lastModifiedDate
	 *            When the prescription is modified, this field contains the
	 *            last modified date. Will have the form YYYY-MM-DD
	 * @param prescriptionEstimatedEndDate
	 *            A calculated, estimated ending date for this prescription.
	 * @param formattedPrescriptionToString
	 *            Contains a formattted textual version of the prescription.
	 * @param prescriptionToString
	 *            Contains an unformatted textual version of the prescription.
	 */
	public ERxPrescription(String doctorLicenseNo, String patientId,
	    String prescriptionId, boolean isDCPrescription,
	    String discontinuedPrescriptionId, String discontinuedUntil,
	    boolean isRenewal, String renewedPrescriptionId,
	    String prescriptionDateTime, String productName, String productForm,
	    String productStrength, long drugCode, float quantity, int qtyUnitId,
	    String qtyUnitToString, int refills, int refillsDuration,
	    PrescriptionTimeUnit refillsDurationTimeUnit, String sigManual,
	    float sigDosage, int sigUnitId, String sigUnitToString,
	    int sigFrequencyId, String sigFrequencyToString, int sigSpecifierId,
	    String sigSpecifierToString, boolean sigPRN, int treatmentDuration,
	    PrescriptionTimeUnit treatmentDurationTimeUnit, String instructions,
	    String manualPrescriptionContent, int narcoticInterval,
	    PrescriptionTimeUnit narcoticIntervalTimeUnit,
	    float narcoticTotalQuantity, String status,
	    PrescriptionStatusId statusID, String ceasingReason,
	    boolean dispenseAsWritten, int dosageVariationIndicator,
	    String endingDate, String lastModifiedByName, String lastModifiedDate,
	    String prescriptionEstimatedEndDate,
	    String formattedPrescriptionToString, String prescriptionToString) {
		super();
		this.doctorLicenseNo = doctorLicenseNo;
		this.patientId = patientId;
		this.prescriptionId = prescriptionId;
		this.isDCPrescription = isDCPrescription;
		this.discontinuedPrescriptionId = discontinuedPrescriptionId;
		this.discontinuedUntil = discontinuedUntil;
		this.isRenewal = isRenewal;
		this.renewedPrescriptionId = renewedPrescriptionId;
		this.prescriptionDateTime = prescriptionDateTime;
		this.productName = productName;
		this.productForm = productForm;
		this.productStrength = productStrength;
		this.drugCode = drugCode;
		this.quantity = quantity;
		this.qtyUnitId = qtyUnitId;
		this.qtyUnitToString = qtyUnitToString;
		this.refills = refills;
		this.refillsDuration = refillsDuration;
		this.refillsDurationTimeUnit = refillsDurationTimeUnit;
		this.sigManual = sigManual;
		this.sigDosage = sigDosage;
		this.sigUnitId = sigUnitId;
		this.sigUnitToString = sigUnitToString;
		this.sigFrequencyId = sigFrequencyId;
		this.sigFrequencyToString = sigFrequencyToString;
		this.sigSpecifierId = sigSpecifierId;
		this.sigSpecifierToString = sigSpecifierToString;
		this.sigPRN = sigPRN;
		this.treatmentDuration = treatmentDuration;
		this.treatmentDurationTimeUnit = treatmentDurationTimeUnit;
		this.instructions = instructions;
		this.manualPrescriptionContent = manualPrescriptionContent;
		this.narcoticInterval = narcoticInterval;
		this.narcoticIntervalTimeUnit = narcoticIntervalTimeUnit;
		this.narcoticTotalQuantity = narcoticTotalQuantity;
		this.status = status;
		this.statusID = statusID;
		this.ceasingReason = ceasingReason;
		this.dispenseAsWritten = dispenseAsWritten;
		this.dosageVariationIndicator = dosageVariationIndicator;
		this.endingDate = endingDate;
		this.lastModifiedByName = lastModifiedByName;
		this.lastModifiedDate = lastModifiedDate;
		this.prescriptionEstimatedEndDate = prescriptionEstimatedEndDate;
		this.formattedPrescriptionToString = formattedPrescriptionToString;
		this.prescriptionToString = prescriptionToString;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		ERxPrescription other = (ERxPrescription) obj;
		if (this.ceasingReason == null) {
			if (other.ceasingReason != null) {
				return false;
			}
		}
		else if (!this.ceasingReason.equals(other.ceasingReason)) {
			return false;
		}
		if (this.discontinuedPrescriptionId == null) {
			if (other.discontinuedPrescriptionId != null) {
				return false;
			}
		}
		else if (!this.discontinuedPrescriptionId
		    .equals(other.discontinuedPrescriptionId)) {
			return false;
		}
		if (this.discontinuedUntil == null) {
			if (other.discontinuedUntil != null) {
				return false;
			}
		}
		else if (!this.discontinuedUntil.equals(other.discontinuedUntil)) {
			return false;
		}
		if (this.dispenseAsWritten != other.dispenseAsWritten) {
			return false;
		}
		if (this.doctorLicenseNo == null) {
			if (other.doctorLicenseNo != null) {
				return false;
			}
		}
		else if (!this.doctorLicenseNo.equals(other.doctorLicenseNo)) {
			return false;
		}
		if (this.dosageVariationIndicator != other.dosageVariationIndicator) {
			return false;
		}
		if (this.drugCode != other.drugCode) {
			return false;
		}
		if (this.endingDate == null) {
			if (other.endingDate != null) {
				return false;
			}
		}
		else if (!this.endingDate.equals(other.endingDate)) {
			return false;
		}
		if (this.formattedPrescriptionToString == null) {
			if (other.formattedPrescriptionToString != null) {
				return false;
			}
		}
		else if (!this.formattedPrescriptionToString
		    .equals(other.formattedPrescriptionToString)) {
			return false;
		}
		if (this.instructions == null) {
			if (other.instructions != null) {
				return false;
			}
		}
		else if (!this.instructions.equals(other.instructions)) {
			return false;
		}
		if (this.isDCPrescription != other.isDCPrescription) {
			return false;
		}
		if (this.isRenewal != other.isRenewal) {
			return false;
		}
		if (this.lastModifiedByName == null) {
			if (other.lastModifiedByName != null) {
				return false;
			}
		}
		else if (!this.lastModifiedByName.equals(other.lastModifiedByName)) {
			return false;
		}
		if (this.lastModifiedDate == null) {
			if (other.lastModifiedDate != null) {
				return false;
			}
		}
		else if (!this.lastModifiedDate.equals(other.lastModifiedDate)) {
			return false;
		}
		if (this.manualPrescriptionContent == null) {
			if (other.manualPrescriptionContent != null) {
				return false;
			}
		}
		else if (!this.manualPrescriptionContent
		    .equals(other.manualPrescriptionContent)) {
			return false;
		}
		if (this.narcoticInterval != other.narcoticInterval) {
			return false;
		}
		if (this.narcoticIntervalTimeUnit != other.narcoticIntervalTimeUnit) {
			return false;
		}
		if (Float.floatToIntBits(this.narcoticTotalQuantity) != Float
		    .floatToIntBits(other.narcoticTotalQuantity)) {
			return false;
		}
		if (this.patientId == null) {
			if (other.patientId != null) {
				return false;
			}
		}
		else if (!this.patientId.equals(other.patientId)) {
			return false;
		}
		if (this.prescriptionDateTime == null) {
			if (other.prescriptionDateTime != null) {
				return false;
			}
		}
		else if (!this.prescriptionDateTime.equals(other.prescriptionDateTime)) {
			return false;
		}
		if (this.prescriptionEstimatedEndDate == null) {
			if (other.prescriptionEstimatedEndDate != null) {
				return false;
			}
		}
		else if (!this.prescriptionEstimatedEndDate
		    .equals(other.prescriptionEstimatedEndDate)) {
			return false;
		}
		if (this.prescriptionId == null) {
			if (other.prescriptionId != null) {
				return false;
			}
		}
		else if (!this.prescriptionId.equals(other.prescriptionId)) {
			return false;
		}
		if (this.prescriptionToString == null) {
			if (other.prescriptionToString != null) {
				return false;
			}
		}
		else if (!this.prescriptionToString.equals(other.prescriptionToString)) {
			return false;
		}
		if (this.productForm == null) {
			if (other.productForm != null) {
				return false;
			}
		}
		else if (!this.productForm.equals(other.productForm)) {
			return false;
		}
		if (this.productName == null) {
			if (other.productName != null) {
				return false;
			}
		}
		else if (!this.productName.equals(other.productName)) {
			return false;
		}
		if (this.productStrength == null) {
			if (other.productStrength != null) {
				return false;
			}
		}
		else if (!this.productStrength.equals(other.productStrength)) {
			return false;
		}
		if (this.qtyUnitId != other.qtyUnitId) {
			return false;
		}
		if (this.qtyUnitToString == null) {
			if (other.qtyUnitToString != null) {
				return false;
			}
		}
		else if (!this.qtyUnitToString.equals(other.qtyUnitToString)) {
			return false;
		}
		if (Float.floatToIntBits(this.quantity) != Float
		    .floatToIntBits(other.quantity)) {
			return false;
		}
		if (this.refills != other.refills) {
			return false;
		}
		if (this.refillsDuration != other.refillsDuration) {
			return false;
		}
		if (this.refillsDurationTimeUnit != other.refillsDurationTimeUnit) {
			return false;
		}
		if (this.renewedPrescriptionId == null) {
			if (other.renewedPrescriptionId != null) {
				return false;
			}
		}
		else if (!this.renewedPrescriptionId
		    .equals(other.renewedPrescriptionId)) {
			return false;
		}
		if (Float.floatToIntBits(this.sigDosage) != Float
		    .floatToIntBits(other.sigDosage)) {
			return false;
		}
		if (this.sigFrequencyId != other.sigFrequencyId) {
			return false;
		}
		if (this.sigFrequencyToString == null) {
			if (other.sigFrequencyToString != null) {
				return false;
			}
		}
		else if (!this.sigFrequencyToString.equals(other.sigFrequencyToString)) {
			return false;
		}
		if (this.sigManual == null) {
			if (other.sigManual != null) {
				return false;
			}
		}
		else if (!this.sigManual.equals(other.sigManual)) {
			return false;
		}
		if (this.sigPRN != other.sigPRN) {
			return false;
		}
		if (this.sigSpecifierId != other.sigSpecifierId) {
			return false;
		}
		if (this.sigSpecifierToString == null) {
			if (other.sigSpecifierToString != null) {
				return false;
			}
		}
		else if (!this.sigSpecifierToString.equals(other.sigSpecifierToString)) {
			return false;
		}
		if (this.sigUnitId != other.sigUnitId) {
			return false;
		}
		if (this.sigUnitToString == null) {
			if (other.sigUnitToString != null) {
				return false;
			}
		}
		else if (!this.sigUnitToString.equals(other.sigUnitToString)) {
			return false;
		}
		if (this.status == null) {
			if (other.status != null) {
				return false;
			}
		}
		else if (!this.status.equals(other.status)) {
			return false;
		}
		if (this.statusID != other.statusID) {
			return false;
		}
		if (this.treatmentDuration != other.treatmentDuration) {
			return false;
		}
		if (this.treatmentDurationTimeUnit != other.treatmentDurationTimeUnit) {
			return false;
		}
		return true;
	}

	/**
	 * @return the ceasingReason
	 */
	public String getCeasingReason() {
		return this.ceasingReason;
	}

	/**
	 * @return the discontinuedPrescriptionId
	 */
	public String getDiscontinuedPrescriptionId() {
		return this.discontinuedPrescriptionId;
	}

	/**
	 * @return the discontinuedUntil
	 */
	public String getDiscontinuedUntil() {
		return this.discontinuedUntil;
	}

	/**
	 * @return the doctorLicenseNo
	 */
	public String getDoctorLicenseNo() {
		return this.doctorLicenseNo;
	}

	/**
	 * @return the dosageVariationIndicator
	 */
	public int getDosageVariationIndicator() {
		return this.dosageVariationIndicator;
	}

	/**
	 * @return the drugCode
	 */
	public long getDrugCode() {
		return this.drugCode;
	}

	/**
	 * @return the endingDate
	 */
	public String getEndingDate() {
		return this.endingDate;
	}

	/**
	 * @return the formattedPrescriptionToString
	 */
	public String getFormattedPrescriptionToString() {
		return this.formattedPrescriptionToString;
	}

	/**
	 * @return the instructions
	 */
	public String getInstructions() {
		return this.instructions;
	}

	/**
	 * @return the lastModifiedByName
	 */
	public String getLastModifiedByName() {
		return this.lastModifiedByName;
	}

	/**
	 * @return the lastModifiedDate
	 */
	public String getLastModifiedDate() {
		return this.lastModifiedDate;
	}

	/**
	 * @return the manualPrescriptionContent
	 */
	public String getManualPrescriptionContent() {
		return this.manualPrescriptionContent;
	}

	/**
	 * @return the narcoticInterval
	 */
	public int getNarcoticInterval() {
		return this.narcoticInterval;
	}

	/**
	 * @return the narcoticIntervalTimeUnit
	 */
	public PrescriptionTimeUnit getNarcoticIntervalTimeUnit() {
		return this.narcoticIntervalTimeUnit;
	}

	/**
	 * @return the narcoticTotalQuantity
	 */
	public float getNarcoticTotalQuantity() {
		return this.narcoticTotalQuantity;
	}

	/**
	 * @return the patientId
	 */
	public String getPatientId() {
		return this.patientId;
	}

	/**
	 * @return the prescriptionDateTime
	 */
	public String getPrescriptionDateTime() {
		return this.prescriptionDateTime;
	}

	/**
	 * @return the prescriptionEstimatedEndDate
	 */
	public String getPrescriptionEstimatedEndDate() {
		return this.prescriptionEstimatedEndDate;
	}

	/**
	 * @return the prescriptionId
	 */
	public String getPrescriptionId() {
		return this.prescriptionId;
	}

	/**
	 * @return the prescriptionToString
	 */
	public String getPrescriptionToString() {
		return this.prescriptionToString;
	}

	/**
	 * @return the productForm
	 */
	public String getProductForm() {
		return this.productForm;
	}

	/**
	 * @return the productName
	 */
	public String getProductName() {
		return this.productName;
	}

	/**
	 * @return the productStrength
	 */
	public String getProductStrength() {
		return this.productStrength;
	}

	/**
	 * @return the qtyUnitId
	 */
	public int getQtyUnitId() {
		return this.qtyUnitId;
	}

	/**
	 * @return the qtyUnitToString
	 */
	public String getQtyUnitToString() {
		return this.qtyUnitToString;
	}

	/**
	 * @return the quantity
	 */
	public float getQuantity() {
		return this.quantity;
	}

	/**
	 * @return the refills
	 */
	public int getRefills() {
		return this.refills;
	}

	/**
	 * @return the refillsDuration
	 */
	public int getRefillsDuration() {
		return this.refillsDuration;
	}

	/**
	 * @return the refillsDurationTimeUnit
	 */
	public PrescriptionTimeUnit getRefillsDurationTimeUnit() {
		return this.refillsDurationTimeUnit;
	}

	/**
	 * @return the renewedPrescriptionId
	 */
	public String getRenewedPrescriptionId() {
		return this.renewedPrescriptionId;
	}

	/**
	 * @return the sigDosage
	 */
	public float getSigDosage() {
		return this.sigDosage;
	}

	/**
	 * @return the sigFrequencyId
	 */
	public int getSigFrequencyId() {
		return this.sigFrequencyId;
	}

	/**
	 * @return the sigFrequencyToString
	 */
	public String getSigFrequencyToString() {
		return this.sigFrequencyToString;
	}

	/**
	 * @return the sigManual
	 */
	public String getSigManual() {
		return this.sigManual;
	}

	/**
	 * @return the sigSpecifierId
	 */
	public int getSigSpecifierId() {
		return this.sigSpecifierId;
	}

	/**
	 * @return the sigSpecifierToString
	 */
	public String getSigSpecifierToString() {
		return this.sigSpecifierToString;
	}

	/**
	 * @return the sigUnitId
	 */
	public int getSigUnitId() {
		return this.sigUnitId;
	}

	/**
	 * @return the sigUnitToString
	 */
	public String getSigUnitToString() {
		return this.sigUnitToString;
	}

	/**
	 * Get a SOAP document fragment representing this object.
	 * 
	 * @return A SOAPElement representing this prescription data.
	 * @throws SOAPException
	 *             If an error occurred when trying to construct the element.
	 */
	public SOAPElement getSOAPElement() throws SOAPException {
		SOAPElement answer =
		    SOAPFactory.newInstance().createElement("WSPrescription5");

		answer.addChildElement("DoctorLicenseNo").addTextNode(
		    this.doctorLicenseNo);
		answer.addChildElement("PatientId").addTextNode(this.patientId);
		answer.addChildElement("PrescriptionId").addTextNode(
		    this.prescriptionId);
		answer.addChildElement("IsDCPrescription").addTextNode(
		    Boolean.toString(this.isDCPrescription));
		answer.addChildElement("DiscontinuedPrescriptionId").addTextNode(
		    this.discontinuedPrescriptionId);
		answer.addChildElement("DiscontinuedUntil").addTextNode(
		    this.discontinuedUntil);
		answer.addChildElement("IsRenewal").addTextNode(
		    Boolean.toString(this.isRenewal));
		answer.addChildElement("RenewedPrescriptionId").addTextNode(
		    this.renewedPrescriptionId);
		answer.addChildElement("PrescriptionDateTime").addTextNode(
		    this.prescriptionDateTime);
		answer.addChildElement("ProductName").addTextNode(this.productName);
		answer.addChildElement("ProductForm").addTextNode(this.productForm);
		answer.addChildElement("ProductStrength").addTextNode(
		    this.productStrength);
		answer.addChildElement("DrugCode").addTextNode(
		    Long.toString(this.drugCode));
		answer.addChildElement("Qty")
		    .addTextNode(Float.toString(this.quantity));
		answer.addChildElement("QtyUnitId").addTextNode(
		    Integer.toString(this.qtyUnitId));
		answer.addChildElement("QtyUnitToString").addTextNode(
		    this.qtyUnitToString);
		answer.addChildElement("Refills").addTextNode(
		    Integer.toString(this.refills));
		answer.addChildElement("RefillsDuration").addTextNode(
		    Integer.toString(this.refillsDuration));
		answer.addChildElement("RefillsDurationTimeUnit").addTextNode(
		    this.refillsDurationTimeUnit.getString());
		answer.addChildElement("SigManual").addTextNode(this.sigManual);
		answer.addChildElement("SigDosage").addTextNode(
		    Float.toString(this.sigDosage));
		answer.addChildElement("SigUnitId").addTextNode(
		    Integer.toString(this.sigUnitId));
		answer.addChildElement("SigUnitToString").addTextNode(
		    this.sigUnitToString);
		answer.addChildElement("SigFrequencyId").addTextNode(
		    Integer.toString(this.sigFrequencyId));
		answer.addChildElement("SigFrequencyToString").addTextNode(
		    this.sigFrequencyToString);
		answer.addChildElement("SigSpecifierId").addTextNode(
		    Integer.toString(this.sigSpecifierId));
		answer.addChildElement("SigSpecifierToString").addTextNode(
		    this.sigSpecifierToString);
		answer.addChildElement("SigPRN").addTextNode(
		    Boolean.toString(this.sigPRN));
		answer.addChildElement("TreatmentDuration").addTextNode(
		    Integer.toString(this.treatmentDuration));
		answer.addChildElement("TreatmentDurationTimeUnit").addTextNode(
		    this.treatmentDurationTimeUnit.getString());
		answer.addChildElement("Instructions").addTextNode(this.instructions);
		answer.addChildElement("ManualPrescriptionContent").addTextNode(
		    this.manualPrescriptionContent);
		answer.addChildElement("NarcoticInterval").addTextNode(
		    Integer.toString(this.narcoticInterval));
		answer.addChildElement("NarcoticIntervalTimeUnit").addTextNode(
		    this.narcoticIntervalTimeUnit.getString());
		answer.addChildElement("NarcoticTotalQuantity").addTextNode(
		    Float.toString(this.narcoticTotalQuantity));
		answer.addChildElement("Status").addTextNode(this.status);
		answer.addChildElement("StatusID").addTextNode(
		    this.statusID.getString());
		answer.addChildElement("CeasingReason").addTextNode(this.ceasingReason);
		answer.addChildElement("DispenseAsWritten").addTextNode(
		    Boolean.toString(this.dispenseAsWritten));
		answer.addChildElement("DosageVariationIndicator").addTextNode(
		    Integer.toString(this.dosageVariationIndicator));
		answer.addChildElement("EndingDate").addTextNode(this.endingDate);
		answer.addChildElement("LastModifiedByName").addTextNode(
		    this.lastModifiedByName);
		answer.addChildElement("LastModifiedDate").addTextNode(
		    this.lastModifiedDate);
		answer.addChildElement("PrescriptionEstimatedEndDate").addTextNode(
		    this.prescriptionEstimatedEndDate);
		answer.addChildElement("FormattedPrescriptionToString").addTextNode(
		    this.formattedPrescriptionToString);
		answer.addChildElement("PrescriptionToString").addTextNode(
		    this.prescriptionToString);

		return answer;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return this.status;
	}

	/**
	 * @return the statusID
	 */
	public PrescriptionStatusId getStatusID() {
		return this.statusID;
	}

	/**
	 * @return the treatmentDuration
	 */
	public int getTreatmentDuration() {
		return this.treatmentDuration;
	}

	/**
	 * @return the treatmentDurationTimeUnit
	 */
	public PrescriptionTimeUnit getTreatmentDurationTimeUnit() {
		return this.treatmentDurationTimeUnit;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result =
		    (prime * result)
		        + ((this.ceasingReason == null) ? 0 : this.ceasingReason
		            .hashCode());
		result =
		    (prime * result)
		        + ((this.discontinuedPrescriptionId == null) ? 0 : this.discontinuedPrescriptionId
		            .hashCode());
		result =
		    (prime * result)
		        + ((this.discontinuedUntil == null) ? 0 : this.discontinuedUntil
		            .hashCode());
		result = (prime * result) + (this.dispenseAsWritten ? 1231 : 1237);
		result =
		    (prime * result)
		        + ((this.doctorLicenseNo == null) ? 0 : this.doctorLicenseNo
		            .hashCode());
		result = (prime * result) + this.dosageVariationIndicator;
		result =
		    (prime * result) + (int) (this.drugCode ^ (this.drugCode >>> 32));
		result =
		    (prime * result)
		        + ((this.endingDate == null) ? 0 : this.endingDate.hashCode());
		result =
		    (prime * result)
		        + ((this.formattedPrescriptionToString == null) ? 0 : this.formattedPrescriptionToString
		            .hashCode());
		result =
		    (prime * result)
		        + ((this.instructions == null) ? 0 : this.instructions
		            .hashCode());
		result = (prime * result) + (this.isDCPrescription ? 1231 : 1237);
		result = (prime * result) + (this.isRenewal ? 1231 : 1237);
		result =
		    (prime * result)
		        + ((this.lastModifiedByName == null) ? 0 : this.lastModifiedByName
		            .hashCode());
		result =
		    (prime * result)
		        + ((this.lastModifiedDate == null) ? 0 : this.lastModifiedDate
		            .hashCode());
		result =
		    (prime * result)
		        + ((this.manualPrescriptionContent == null) ? 0 : this.manualPrescriptionContent
		            .hashCode());
		result = (prime * result) + this.narcoticInterval;
		result =
		    (prime * result)
		        + ((this.narcoticIntervalTimeUnit == null) ? 0 : this.narcoticIntervalTimeUnit
		            .hashCode());
		result =
		    (prime * result) + Float.floatToIntBits(this.narcoticTotalQuantity);
		result =
		    (prime * result)
		        + ((this.patientId == null) ? 0 : this.patientId.hashCode());
		result =
		    (prime * result)
		        + ((this.prescriptionDateTime == null) ? 0 : this.prescriptionDateTime
		            .hashCode());
		result =
		    (prime * result)
		        + ((this.prescriptionEstimatedEndDate == null) ? 0 : this.prescriptionEstimatedEndDate
		            .hashCode());
		result =
		    (prime * result)
		        + ((this.prescriptionId == null) ? 0 : this.prescriptionId
		            .hashCode());
		result =
		    (prime * result)
		        + ((this.prescriptionToString == null) ? 0 : this.prescriptionToString
		            .hashCode());
		result =
		    (prime * result)
		        + ((this.productForm == null) ? 0 : this.productForm.hashCode());
		result =
		    (prime * result)
		        + ((this.productName == null) ? 0 : this.productName.hashCode());
		result =
		    (prime * result)
		        + ((this.productStrength == null) ? 0 : this.productStrength
		            .hashCode());
		result = (prime * result) + this.qtyUnitId;
		result =
		    (prime * result)
		        + ((this.qtyUnitToString == null) ? 0 : this.qtyUnitToString
		            .hashCode());
		result = (prime * result) + Float.floatToIntBits(this.quantity);
		result = (prime * result) + this.refills;
		result = (prime * result) + this.refillsDuration;
		result =
		    (prime * result)
		        + ((this.refillsDurationTimeUnit == null) ? 0 : this.refillsDurationTimeUnit
		            .hashCode());
		result =
		    (prime * result)
		        + ((this.renewedPrescriptionId == null) ? 0 : this.renewedPrescriptionId
		            .hashCode());
		result = (prime * result) + Float.floatToIntBits(this.sigDosage);
		result = (prime * result) + this.sigFrequencyId;
		result =
		    (prime * result)
		        + ((this.sigFrequencyToString == null) ? 0 : this.sigFrequencyToString
		            .hashCode());
		result =
		    (prime * result)
		        + ((this.sigManual == null) ? 0 : this.sigManual.hashCode());
		result = (prime * result) + (this.sigPRN ? 1231 : 1237);
		result = (prime * result) + this.sigSpecifierId;
		result =
		    (prime * result)
		        + ((this.sigSpecifierToString == null) ? 0 : this.sigSpecifierToString
		            .hashCode());
		result = (prime * result) + this.sigUnitId;
		result =
		    (prime * result)
		        + ((this.sigUnitToString == null) ? 0 : this.sigUnitToString
		            .hashCode());
		result =
		    (prime * result)
		        + ((this.status == null) ? 0 : this.status.hashCode());
		result =
		    (prime * result)
		        + ((this.statusID == null) ? 0 : this.statusID.hashCode());
		result = (prime * result) + this.treatmentDuration;
		result =
		    (prime * result)
		        + ((this.treatmentDurationTimeUnit == null) ? 0 : this.treatmentDurationTimeUnit
		            .hashCode());
		return result;
	}

	/**
	 * @return the isDCPrescription
	 */
	public boolean isDCPrescription() {
		return this.isDCPrescription;
	}

	/**
	 * @return the dispenseAsWritten
	 */
	public boolean isDispenseAsWritten() {
		return this.dispenseAsWritten;
	}

	/**
	 * @return the isRenewal
	 */
	public boolean isRenewal() {
		return this.isRenewal;
	}

	/**
	 * @return the sigPRN
	 */
	public boolean isSigPRN() {
		return this.sigPRN;
	}

	/**
	 * @param ceasingReason
	 *            the ceasingReason to set
	 */
	public void setCeasingReason(String ceasingReason) {
		this.ceasingReason = ceasingReason;
	}

	/**
	 * @param isDCPrescription
	 *            the isDCPrescription to set
	 */
	public void setDCPrescription(boolean isDCPrescription) {
		this.isDCPrescription = isDCPrescription;
	}

	/**
	 * @param discontinuedPrescriptionId
	 *            the discontinuedPrescriptionId to set
	 */
	public void setDiscontinuedPrescriptionId(String discontinuedPrescriptionId) {
		this.discontinuedPrescriptionId = discontinuedPrescriptionId;
	}

	/**
	 * @param discontinuedUntil
	 *            the discontinuedUntil to set
	 */
	public void setDiscontinuedUntil(String discontinuedUntil) {
		this.discontinuedUntil = discontinuedUntil;
	}

	/**
	 * @param dispenseAsWritten
	 *            the dispenseAsWritten to set
	 */
	public void setDispenseAsWritten(boolean dispenseAsWritten) {
		this.dispenseAsWritten = dispenseAsWritten;
	}

	/**
	 * @param doctorLicenseNo
	 *            the doctorLicenseNo to set
	 */
	public void setDoctorLicenseNo(String doctorLicenseNo) {
		this.doctorLicenseNo = doctorLicenseNo;
	}

	/**
	 * @param dosageVariationIndicator
	 *            the dosageVariationIndicator to set
	 */
	public void setDosageVariationIndicator(int dosageVariationIndicator) {
		this.dosageVariationIndicator = dosageVariationIndicator;
	}

	/**
	 * @param drugCode
	 *            the drugCode to set
	 */
	public void setDrugCode(long drugCode) {
		this.drugCode = drugCode;
	}

	/**
	 * @param endingDate
	 *            the endingDate to set
	 */
	public void setEndingDate(String endingDate) {
		this.endingDate = endingDate;
	}

	/**
	 * @param formattedPrescriptionToString
	 *            the formattedPrescriptionToString to set
	 */
	public void setFormattedPrescriptionToString(
	    String formattedPrescriptionToString) {
		this.formattedPrescriptionToString = formattedPrescriptionToString;
	}

	/**
	 * @param instructions
	 *            the instructions to set
	 */
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}

	/**
	 * @param lastModifiedByName
	 *            the lastModifiedByName to set
	 */
	public void setLastModifiedByName(String lastModifiedByName) {
		this.lastModifiedByName = lastModifiedByName;
	}

	/**
	 * @param lastModifiedDate
	 *            the lastModifiedDate to set
	 */
	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	/**
	 * @param manualPrescriptionContent
	 *            the manualPrescriptionContent to set
	 */
	public void setManualPrescriptionContent(String manualPrescriptionContent) {
		this.manualPrescriptionContent = manualPrescriptionContent;
	}

	/**
	 * @param narcoticInterval
	 *            the narcoticInterval to set
	 */
	public void setNarcoticInterval(int narcoticInterval) {
		this.narcoticInterval = narcoticInterval;
	}

	/**
	 * @param narcoticIntervalTimeUnit
	 *            the narcoticIntervalTimeUnit to set
	 */
	public void setNarcoticIntervalTimeUnit(
	    PrescriptionTimeUnit narcoticIntervalTimeUnit) {
		this.narcoticIntervalTimeUnit = narcoticIntervalTimeUnit;
	}

	/**
	 * @param narcoticTotalQuantity
	 *            the narcoticTotalQuantity to set
	 */
	public void setNarcoticTotalQuantity(float narcoticTotalQuantity) {
		this.narcoticTotalQuantity = narcoticTotalQuantity;
	}

	/**
	 * @param patientId
	 *            the patientId to set
	 */
	public void setPatientId(String patientId) {
		this.patientId = patientId;
	}

	/**
	 * @param prescriptionDateTime
	 *            the prescriptionDateTime to set
	 */
	public void setPrescriptionDateTime(String prescriptionDateTime) {
		this.prescriptionDateTime = prescriptionDateTime;
	}

	/**
	 * @param prescriptionEstimatedEndDate
	 *            the prescriptionEstimatedEndDate to set
	 */
	public void setPrescriptionEstimatedEndDate(
	    String prescriptionEstimatedEndDate) {
		this.prescriptionEstimatedEndDate = prescriptionEstimatedEndDate;
	}

	/**
	 * @param prescriptionId
	 *            the prescriptionId to set
	 */
	public void setPrescriptionId(String prescriptionId) {
		this.prescriptionId = prescriptionId;
	}

	/**
	 * @param prescriptionToString
	 *            the prescriptionToString to set
	 */
	public void setPrescriptionToString(String prescriptionToString) {
		this.prescriptionToString = prescriptionToString;
	}

	/**
	 * @param productForm
	 *            the productForm to set
	 */
	public void setProductForm(String productForm) {
		this.productForm = productForm;
	}

	/**
	 * @param productName
	 *            the productName to set
	 */
	public void setProductName(String productName) {
		this.productName = productName;
	}

	/**
	 * @param productStrength
	 *            the productStrength to set
	 */
	public void setProductStrength(String productStrength) {
		this.productStrength = productStrength;
	}

	/**
	 * @param qtyUnitId
	 *            the qtyUnitId to set
	 */
	public void setQtyUnitId(int qtyUnitId) {
		this.qtyUnitId = qtyUnitId;
	}

	/**
	 * @param qtyUnitToString
	 *            the qtyUnitToString to set
	 */
	public void setQtyUnitToString(String qtyUnitToString) {
		this.qtyUnitToString = qtyUnitToString;
	}

	/**
	 * @param quantity
	 *            the quantity to set
	 */
	public void setQuantity(float quantity) {
		this.quantity = quantity;
	}

	/**
	 * @param refills
	 *            the refills to set
	 */
	public void setRefills(int refills) {
		this.refills = refills;
	}

	/**
	 * @param refillsDuration
	 *            the refillsDuration to set
	 */
	public void setRefillsDuration(int refillsDuration) {
		this.refillsDuration = refillsDuration;
	}

	/**
	 * @param refillsDurationTimeUnit
	 *            the refillsDurationTimeUnit to set
	 */
	public void setRefillsDurationTimeUnit(
	    PrescriptionTimeUnit refillsDurationTimeUnit) {
		this.refillsDurationTimeUnit = refillsDurationTimeUnit;
	}

	/**
	 * @param isRenewal
	 *            the isRenewal to set
	 */
	public void setRenewal(boolean isRenewal) {
		this.isRenewal = isRenewal;
	}

	/**
	 * @param renewedPrescriptionId
	 *            the renewedPrescriptionId to set
	 */
	public void setRenewedPrescriptionId(String renewedPrescriptionId) {
		this.renewedPrescriptionId = renewedPrescriptionId;
	}

	/**
	 * @param sigDosage
	 *            the sigDosage to set
	 */
	public void setSigDosage(float sigDosage) {
		this.sigDosage = sigDosage;
	}

	/**
	 * @param sigFrequencyId
	 *            the sigFrequencyId to set
	 */
	public void setSigFrequencyId(int sigFrequencyId) {
		this.sigFrequencyId = sigFrequencyId;
	}

	/**
	 * @param sigFrequencyToString
	 *            the sigFrequencyToString to set
	 */
	public void setSigFrequencyToString(String sigFrequencyToString) {
		this.sigFrequencyToString = sigFrequencyToString;
	}

	/**
	 * @param sigManual
	 *            the sigManual to set
	 */
	public void setSigManual(String sigManual) {
		this.sigManual = sigManual;
	}

	/**
	 * @param sigPRN
	 *            the sigPRN to set
	 */
	public void setSigPRN(boolean sigPRN) {
		this.sigPRN = sigPRN;
	}

	/**
	 * @param sigSpecifierId
	 *            the sigSpecifierId to set
	 */
	public void setSigSpecifierId(int sigSpecifierId) {
		this.sigSpecifierId = sigSpecifierId;
	}

	/**
	 * @param sigSpecifierToString
	 *            the sigSpecifierToString to set
	 */
	public void setSigSpecifierToString(String sigSpecifierToString) {
		this.sigSpecifierToString = sigSpecifierToString;
	}

	/**
	 * @param sigUnitId
	 *            the sigUnitId to set
	 */
	public void setSigUnitId(int sigUnitId) {
		this.sigUnitId = sigUnitId;
	}

	/**
	 * @param sigUnitToString
	 *            the sigUnitToString to set
	 */
	public void setSigUnitToString(String sigUnitToString) {
		this.sigUnitToString = sigUnitToString;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @param statusID
	 *            the statusID to set
	 */
	public void setStatusID(PrescriptionStatusId statusID) {
		this.statusID = statusID;
	}

	/**
	 * @param treatmentDuration
	 *            the treatmentDuration to set
	 */
	public void setTreatmentDuration(int treatmentDuration) {
		this.treatmentDuration = treatmentDuration;
	}

	/**
	 * @param treatmentDurationTimeUnit
	 *            the treatmentDurationTimeUnit to set
	 */
	public void setTreatmentDurationTimeUnit(
	    PrescriptionTimeUnit treatmentDurationTimeUnit) {
		this.treatmentDurationTimeUnit = treatmentDurationTimeUnit;
	}
}
