/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
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
package oscar.form.pdfservlet;

import java.util.Properties;

public class ONAR1EnhancedPostProcessor implements FrmPDFPostValueProcessor {

	@Override
	public Properties process(Properties p) {
		if(p.getProperty("pg1_language","").equals("ENG")) {p.setProperty("pg1_language", "English");}
		if(p.getProperty("pg1_language","").equals("FRA")) {p.setProperty("pg1_language", "French");}
		if(p.getProperty("pg1_language","").equals("AAR")) {p.setProperty("pg1_language", "Afar");}
		if(p.getProperty("pg1_language","").equals("AFR")) {p.setProperty("pg1_language", "Afrikaans");}
		if(p.getProperty("pg1_language","").equals("AKA")) {p.setProperty("pg1_language", "Akan");}
		if(p.getProperty("pg1_language","").equals("SQI")) {p.setProperty("pg1_language", "Albanian");}
		if(p.getProperty("pg1_language","").equals("ASE")) {p.setProperty("pg1_language", "American Sign Language (ASL)");}
		if(p.getProperty("pg1_language","").equals("AMH")) {p.setProperty("pg1_language", "Amharic");}
		if(p.getProperty("pg1_language","").equals("ARA")) {p.setProperty("pg1_language", "Arabic");}
		if(p.getProperty("pg1_language","").equals("ARG")) {p.setProperty("pg1_language", "Aragonese");}
		if(p.getProperty("pg1_language","").equals("HYE")) {p.setProperty("pg1_language", "Armenian");}
		if(p.getProperty("pg1_language","").equals("ASM")) {p.setProperty("pg1_language", "Assamese");}
		if(p.getProperty("pg1_language","").equals("AVA")) {p.setProperty("pg1_language", "Avaric");}
		if(p.getProperty("pg1_language","").equals("AYM")) {p.setProperty("pg1_language", "Aymara");}
		if(p.getProperty("pg1_language","").equals("AZE")) {p.setProperty("pg1_language", "Azerbaijani");}
		if(p.getProperty("pg1_language","").equals("BAM")) {p.setProperty("pg1_language", "Bambara");}
		if(p.getProperty("pg1_language","").equals("BAK")) {p.setProperty("pg1_language", "Bashkir");}
		if(p.getProperty("pg1_language","").equals("EUS")) {p.setProperty("pg1_language", "Basque");}
		if(p.getProperty("pg1_language","").equals("BEL")) {p.setProperty("pg1_language", "Belarusian");}
		if(p.getProperty("pg1_language","").equals("BEN")) {p.setProperty("pg1_language", "Bengali");}
		if(p.getProperty("pg1_language","").equals("BIS")) {p.setProperty("pg1_language", "Bislama");}
		if(p.getProperty("pg1_language","").equals("BOS")) {p.setProperty("pg1_language", "Bosnian");}
		if(p.getProperty("pg1_language","").equals("BRE")) {p.setProperty("pg1_language", "Breton");}
		if(p.getProperty("pg1_language","").equals("BUL")) {p.setProperty("pg1_language", "Bulgarian");}
		if(p.getProperty("pg1_language","").equals("MYA")) {p.setProperty("pg1_language", "Burmese");}
		if(p.getProperty("pg1_language","").equals("CAT")) {p.setProperty("pg1_language", "Catalan");}
		if(p.getProperty("pg1_language","").equals("KHM")) {p.setProperty("pg1_language", "Central Khmer");}
		if(p.getProperty("pg1_language","").equals("CHA")) {p.setProperty("pg1_language", "Chamorro");}
		if(p.getProperty("pg1_language","").equals("CHE")) {p.setProperty("pg1_language", "Chechen");}
		if(p.getProperty("pg1_language","").equals("YUE")) {p.setProperty("pg1_language", "Chinese Cantonese");}
		if(p.getProperty("pg1_language","").equals("CMN")) {p.setProperty("pg1_language", "Chinese Mandarin");}
		if(p.getProperty("pg1_language","").equals("CHV")) {p.setProperty("pg1_language", "Chuvash");}
		if(p.getProperty("pg1_language","").equals("COR")) {p.setProperty("pg1_language", "Cornish");}
		if(p.getProperty("pg1_language","").equals("COS")) {p.setProperty("pg1_language", "Corsican");}
		if(p.getProperty("pg1_language","").equals("CRE")) {p.setProperty("pg1_language", "Cree");}
		if(p.getProperty("pg1_language","").equals("HRV")) {p.setProperty("pg1_language", "Croatian");}
		if(p.getProperty("pg1_language","").equals("CES")) {p.setProperty("pg1_language", "Czech");}
		if(p.getProperty("pg1_language","").equals("DAN")) {p.setProperty("pg1_language", "Danish");}
		if(p.getProperty("pg1_language","").equals("DIV")) {p.setProperty("pg1_language", "Dhivehi");}
		if(p.getProperty("pg1_language","").equals("NLD")) {p.setProperty("pg1_language", "Dutch");}
		if(p.getProperty("pg1_language","").equals("DZO")) {p.setProperty("pg1_language", "Dzongkha");}
		if(p.getProperty("pg1_language","").equals("EST")) {p.setProperty("pg1_language", "Estonian");}
		if(p.getProperty("pg1_language","").equals("EWE")) {p.setProperty("pg1_language", "Ewe");}
		if(p.getProperty("pg1_language","").equals("FAO")) {p.setProperty("pg1_language", "Faroese");}
		if(p.getProperty("pg1_language","").equals("FIJ")) {p.setProperty("pg1_language", "Fijian");}
		if(p.getProperty("pg1_language","").equals("FIL")) {p.setProperty("pg1_language", "Filipino");}
		if(p.getProperty("pg1_language","").equals("FIN")) {p.setProperty("pg1_language", "Finnish");}
		if(p.getProperty("pg1_language","").equals("FUL")) {p.setProperty("pg1_language", "Fulah");}
		if(p.getProperty("pg1_language","").equals("GLG")) {p.setProperty("pg1_language", "Galician");}
		if(p.getProperty("pg1_language","").equals("LUG")) {p.setProperty("pg1_language", "Ganda");}
		if(p.getProperty("pg1_language","").equals("KAT")) {p.setProperty("pg1_language", "Georgian");}
		if(p.getProperty("pg1_language","").equals("DEU")) {p.setProperty("pg1_language", "German");}
		if(p.getProperty("pg1_language","").equals("GRN")) {p.setProperty("pg1_language", "Guarani");}
		if(p.getProperty("pg1_language","").equals("GUJ")) {p.setProperty("pg1_language", "Gujarati");}
		if(p.getProperty("pg1_language","").equals("HAT")) {p.setProperty("pg1_language", "Haitian");}
		if(p.getProperty("pg1_language","").equals("HAU")) {p.setProperty("pg1_language", "Hausa");}
		if(p.getProperty("pg1_language","").equals("HEB")) {p.setProperty("pg1_language", "Hebrew");}
		if(p.getProperty("pg1_language","").equals("HER")) {p.setProperty("pg1_language", "Herero");}
		if(p.getProperty("pg1_language","").equals("HIN")) {p.setProperty("pg1_language", "Hindi");}
		if(p.getProperty("pg1_language","").equals("HMO")) {p.setProperty("pg1_language", "Hiri Motu");}
		if(p.getProperty("pg1_language","").equals("HUN")) {p.setProperty("pg1_language", "Hungarian");}
		if(p.getProperty("pg1_language","").equals("ISL")) {p.setProperty("pg1_language", "Icelandic");}
		if(p.getProperty("pg1_language","").equals("IBO")) {p.setProperty("pg1_language", "Igbo");}
		if(p.getProperty("pg1_language","").equals("IND")) {p.setProperty("pg1_language", "Indonesian");}
		if(p.getProperty("pg1_language","").equals("IKU")) {p.setProperty("pg1_language", "Inuktitut");}
		if(p.getProperty("pg1_language","").equals("IPK")) {p.setProperty("pg1_language", "Inupiaq");}
		if(p.getProperty("pg1_language","").equals("GLE")) {p.setProperty("pg1_language", "Irish");}
		if(p.getProperty("pg1_language","").equals("ITA")) {p.setProperty("pg1_language", "Italian");}
		if(p.getProperty("pg1_language","").equals("JPN")) {p.setProperty("pg1_language", "Japanese");}
		if(p.getProperty("pg1_language","").equals("JAV")) {p.setProperty("pg1_language", "Javanese");}
		if(p.getProperty("pg1_language","").equals("KAL")) {p.setProperty("pg1_language", "Kalaallisut");}
		if(p.getProperty("pg1_language","").equals("KAN")) {p.setProperty("pg1_language", "Kannada");}
		if(p.getProperty("pg1_language","").equals("KAU")) {p.setProperty("pg1_language", "Kanuri");}
		if(p.getProperty("pg1_language","").equals("KAS")) {p.setProperty("pg1_language", "Kashmiri");}
		if(p.getProperty("pg1_language","").equals("KAZ")) {p.setProperty("pg1_language", "Kazakh");}
		if(p.getProperty("pg1_language","").equals("KIK")) {p.setProperty("pg1_language", "Kikuyu");}
		if(p.getProperty("pg1_language","").equals("KIN")) {p.setProperty("pg1_language", "Kinyarwanda");}
		if(p.getProperty("pg1_language","").equals("KIR")) {p.setProperty("pg1_language", "Kirghiz");}
		if(p.getProperty("pg1_language","").equals("KOM")) {p.setProperty("pg1_language", "Komi");}
		if(p.getProperty("pg1_language","").equals("KON")) {p.setProperty("pg1_language", "Kongo");}
		if(p.getProperty("pg1_language","").equals("KOR")) {p.setProperty("pg1_language", "Korean");}
		if(p.getProperty("pg1_language","").equals("KUA")) {p.setProperty("pg1_language", "Kuanyama");}
		if(p.getProperty("pg1_language","").equals("KUR")) {p.setProperty("pg1_language", "Kurdish");}
		if(p.getProperty("pg1_language","").equals("LAO")) {p.setProperty("pg1_language", "Lao");}
		if(p.getProperty("pg1_language","").equals("LAV")) {p.setProperty("pg1_language", "Latvian");}
		if(p.getProperty("pg1_language","").equals("LIM")) {p.setProperty("pg1_language", "Limburgan");}
		if(p.getProperty("pg1_language","").equals("LIN")) {p.setProperty("pg1_language", "Lingala");}
		if(p.getProperty("pg1_language","").equals("LIT")) {p.setProperty("pg1_language", "Lithuanian");}
		if(p.getProperty("pg1_language","").equals("LUB")) {p.setProperty("pg1_language", "Luba-Katanga");}
		if(p.getProperty("pg1_language","").equals("LTZ")) {p.setProperty("pg1_language", "Luxembourgish");}
		if(p.getProperty("pg1_language","").equals("MKD")) {p.setProperty("pg1_language", "Macedonian");}
		if(p.getProperty("pg1_language","").equals("MLG")) {p.setProperty("pg1_language", "Malagasy");}
		if(p.getProperty("pg1_language","").equals("MSA")) {p.setProperty("pg1_language", "Malay");}
		if(p.getProperty("pg1_language","").equals("MAL")) {p.setProperty("pg1_language", "Malayalam");}
		if(p.getProperty("pg1_language","").equals("MLT")) {p.setProperty("pg1_language", "Maltese");}
		if(p.getProperty("pg1_language","").equals("GLV")) {p.setProperty("pg1_language", "Manx");}
		if(p.getProperty("pg1_language","").equals("MRI")) {p.setProperty("pg1_language", "Maori");}
		if(p.getProperty("pg1_language","").equals("MAR")) {p.setProperty("pg1_language", "Marathi");}
		if(p.getProperty("pg1_language","").equals("MAH")) {p.setProperty("pg1_language", "Marshallese");}
		if(p.getProperty("pg1_language","").equals("ELL")) {p.setProperty("pg1_language", "Greek");}
		if(p.getProperty("pg1_language","").equals("MON")) {p.setProperty("pg1_language", "Mongolian");}
		if(p.getProperty("pg1_language","").equals("NAU")) {p.setProperty("pg1_language", "Nauru");}
		if(p.getProperty("pg1_language","").equals("NAV")) {p.setProperty("pg1_language", "Navajo");}
		if(p.getProperty("pg1_language","").equals("NDO")) {p.setProperty("pg1_language", "Ndonga");}
		if(p.getProperty("pg1_language","").equals("NEP")) {p.setProperty("pg1_language", "Nepali");}
		if(p.getProperty("pg1_language","").equals("NDE")) {p.setProperty("pg1_language", "North Ndebele");}
		if(p.getProperty("pg1_language","").equals("SME")) {p.setProperty("pg1_language", "Northern Sami");}
		if(p.getProperty("pg1_language","").equals("NOR")) {p.setProperty("pg1_language", "Norwegian");}
		if(p.getProperty("pg1_language","").equals("NOB")) {p.setProperty("pg1_language", "Norwegian Bokmål");}
		if(p.getProperty("pg1_language","").equals("NNO")) {p.setProperty("pg1_language", "Norwegian Nynorsk");}
		if(p.getProperty("pg1_language","").equals("NYA")) {p.setProperty("pg1_language", "Nyanja");}
		if(p.getProperty("pg1_language","").equals("OCI")) {p.setProperty("pg1_language", "Occitan (post 1500)");}
		if(p.getProperty("pg1_language","").equals("OJI")) {p.setProperty("pg1_language", "Ojibwa");}
		if(p.getProperty("pg1_language","").equals("OJC")) {p.setProperty("pg1_language", "Oji-cree");}
		if(p.getProperty("pg1_language","").equals("ORI")) {p.setProperty("pg1_language", "Oriya");}
		if(p.getProperty("pg1_language","").equals("ORM")) {p.setProperty("pg1_language", "Oromo");}
		if(p.getProperty("pg1_language","").equals("OSS")) {p.setProperty("pg1_language", "Ossetian");}
		if(p.getProperty("pg1_language","").equals("PAN")) {p.setProperty("pg1_language", "Panjabi");}
		if(p.getProperty("pg1_language","").equals("FAS")) {p.setProperty("pg1_language", "Persian");}
		if(p.getProperty("pg1_language","").equals("POL")) {p.setProperty("pg1_language", "Polish");}
		if(p.getProperty("pg1_language","").equals("POR")) {p.setProperty("pg1_language", "Portuguese");}
		if(p.getProperty("pg1_language","").equals("PUS")) {p.setProperty("pg1_language", "Pushto");}
		if(p.getProperty("pg1_language","").equals("QUE")) {p.setProperty("pg1_language", "Quechua");}
		if(p.getProperty("pg1_language","").equals("RON")) {p.setProperty("pg1_language", "Romanian");}
		if(p.getProperty("pg1_language","").equals("ROH")) {p.setProperty("pg1_language", "Romansh");}
		if(p.getProperty("pg1_language","").equals("RUN")) {p.setProperty("pg1_language", "Rundi");}
		if(p.getProperty("pg1_language","").equals("RUS")) {p.setProperty("pg1_language", "Russian");}
		if(p.getProperty("pg1_language","").equals("SMO")) {p.setProperty("pg1_language", "Samoan");}
		if(p.getProperty("pg1_language","").equals("SAG")) {p.setProperty("pg1_language", "Sango");}
		if(p.getProperty("pg1_language","").equals("SRD")) {p.setProperty("pg1_language", "Sardinian");}
		if(p.getProperty("pg1_language","").equals("GLA")) {p.setProperty("pg1_language", "Scottish Gaelic");}
		if(p.getProperty("pg1_language","").equals("SRP")) {p.setProperty("pg1_language", "Serbian");}
		if(p.getProperty("pg1_language","").equals("SNA")) {p.setProperty("pg1_language", "Shona");}
		if(p.getProperty("pg1_language","").equals("III")) {p.setProperty("pg1_language", "Sichuan Yi");}
		if(p.getProperty("pg1_language","").equals("SND")) {p.setProperty("pg1_language", "Sindhi");}
		if(p.getProperty("pg1_language","").equals("SIN")) {p.setProperty("pg1_language", "Sinhala");}
		if(p.getProperty("pg1_language","").equals("SGN")) {p.setProperty("pg1_language", "Other Sign Language");}
		if(p.getProperty("pg1_language","").equals("SLK")) {p.setProperty("pg1_language", "Slovak");}
		if(p.getProperty("pg1_language","").equals("SLV")) {p.setProperty("pg1_language", "Slovenian");}
		if(p.getProperty("pg1_language","").equals("SOM")) {p.setProperty("pg1_language", "Somali");}
		if(p.getProperty("pg1_language","").equals("NBL")) {p.setProperty("pg1_language", "South Ndebele");}
		if(p.getProperty("pg1_language","").equals("SOT")) {p.setProperty("pg1_language", "Southern Sotho");}
		if(p.getProperty("pg1_language","").equals("SPA")) {p.setProperty("pg1_language", "Spanish");}
		if(p.getProperty("pg1_language","").equals("SUN")) {p.setProperty("pg1_language", "Sundanese");}
		if(p.getProperty("pg1_language","").equals("SWA")) {p.setProperty("pg1_language", "Swahili (macrolanguage)");}
		if(p.getProperty("pg1_language","").equals("SSW")) {p.setProperty("pg1_language", "Swati");}
		if(p.getProperty("pg1_language","").equals("SWE")) {p.setProperty("pg1_language", "Swedish");}
		if(p.getProperty("pg1_language","").equals("TGL")) {p.setProperty("pg1_language", "Tagalog");}
		if(p.getProperty("pg1_language","").equals("TAH")) {p.setProperty("pg1_language", "Tahitian");}
		if(p.getProperty("pg1_language","").equals("TGK")) {p.setProperty("pg1_language", "Tajik");}
		if(p.getProperty("pg1_language","").equals("TAM")) {p.setProperty("pg1_language", "Tamil");}
		if(p.getProperty("pg1_language","").equals("TAT")) {p.setProperty("pg1_language", "Tatar");}
		if(p.getProperty("pg1_language","").equals("TEL")) {p.setProperty("pg1_language", "Telugu");}
		if(p.getProperty("pg1_language","").equals("THA")) {p.setProperty("pg1_language", "Thai");}
		if(p.getProperty("pg1_language","").equals("BOD")) {p.setProperty("pg1_language", "Tibetan");}
		if(p.getProperty("pg1_language","").equals("TIR")) {p.setProperty("pg1_language", "Tigrinya");}
		if(p.getProperty("pg1_language","").equals("TON")) {p.setProperty("pg1_language", "Tonga (Tonga Islands)");}
		if(p.getProperty("pg1_language","").equals("TSO")) {p.setProperty("pg1_language", "Tsonga");}
		if(p.getProperty("pg1_language","").equals("TSN")) {p.setProperty("pg1_language", "Tswana");}
		if(p.getProperty("pg1_language","").equals("TUR")) {p.setProperty("pg1_language", "Turkish");}
		if(p.getProperty("pg1_language","").equals("TUK")) {p.setProperty("pg1_language", "Turkmen");}
		if(p.getProperty("pg1_language","").equals("TWI")) {p.setProperty("pg1_language", "Twi");}
		if(p.getProperty("pg1_language","").equals("UIG")) {p.setProperty("pg1_language", "Uighur");}
		if(p.getProperty("pg1_language","").equals("UKR")) {p.setProperty("pg1_language", "Ukrainian");}
		if(p.getProperty("pg1_language","").equals("URD")) {p.setProperty("pg1_language", "Urdu");}
		if(p.getProperty("pg1_language","").equals("UZB")) {p.setProperty("pg1_language", "Uzbek");}
		if(p.getProperty("pg1_language","").equals("VEN")) {p.setProperty("pg1_language", "Venda");}
		if(p.getProperty("pg1_language","").equals("VIE")) {p.setProperty("pg1_language", "Vietnamese");}
		if(p.getProperty("pg1_language","").equals("WLN")) {p.setProperty("pg1_language", "Walloon");}
		if(p.getProperty("pg1_language","").equals("CYM")) {p.setProperty("pg1_language", "Welsh");}
		if(p.getProperty("pg1_language","").equals("FRY")) {p.setProperty("pg1_language", "Western Frisian");}
		if(p.getProperty("pg1_language","").equals("WOL")) {p.setProperty("pg1_language", "Wolof");}
		if(p.getProperty("pg1_language","").equals("XHO")) {p.setProperty("pg1_language", "Xhosa");}
		if(p.getProperty("pg1_language","").equals("YID")) {p.setProperty("pg1_language", "Yiddish");}
		if(p.getProperty("pg1_language","").equals("YOR")) {p.setProperty("pg1_language", "Yoruba");}
		if(p.getProperty("pg1_language","").equals("ZHA")) {p.setProperty("pg1_language", "Zhuang");}
		if(p.getProperty("pg1_language","").equals("ZUL")) {p.setProperty("pg1_language", "Zulu");}
		if(p.getProperty("pg1_language","").equals("OTH")) {p.setProperty("pg1_language", "Other");}
		if(p.getProperty("pg1_language","").equals("UN")) {p.setProperty("pg1_language", "Unknown");}


		if(p.getProperty("pg1_partnerEduLevel","").equals("UNK")) {p.setProperty("pg1_partnerEduLevel", "");}
		if(p.getProperty("pg1_partnerEduLevel","").equals("UN")) {p.setProperty("pg1_partnerEduLevel", "");}
		if(p.getProperty("pg1_partnerEduLevel","").equals("ED001")) {p.setProperty("pg1_partnerEduLevel", "Post Secondary");}		
		if(p.getProperty("pg1_partnerEduLevel","").equals("ED002")) {p.setProperty("pg1_partnerEduLevel", "Post Sec. N/Completed");}
		if(p.getProperty("pg1_partnerEduLevel","").equals("ED003")) {p.setProperty("pg1_partnerEduLevel", "Grade School Completed");}
		if(p.getProperty("pg1_partnerEduLevel","").equals("ED004")) {p.setProperty("pg1_partnerEduLevel", "High School Completed");}
		if(p.getProperty("pg1_partnerEduLevel","").equals("ED005")) {p.setProperty("pg1_partnerEduLevel", "High School N/Completed");}
		if(p.getProperty("pg1_partnerEduLevel","").equals("ED006")) {p.setProperty("pg1_partnerEduLevel", "Grade School N/Completed");}

		//max 19
		if(p.getProperty("pg1_eduLevel","").equals("UN")) {p.setProperty("pg1_eduLevel", "");}		
		if(p.getProperty("pg1_eduLevel","").equals("UNK")) {p.setProperty("pg1_eduLevel", "");}
		if(p.getProperty("pg1_eduLevel","").equals("ED001")) {p.setProperty("pg1_eduLevel", "Post Secondary");}		
		if(p.getProperty("pg1_eduLevel","").equals("ED002")) {p.setProperty("pg1_eduLevel", "Post Sec. N/Completed");}
		if(p.getProperty("pg1_eduLevel","").equals("ED003")) {p.setProperty("pg1_eduLevel", "Grade School Completed");}
		if(p.getProperty("pg1_eduLevel","").equals("ED004")) {p.setProperty("pg1_eduLevel", "High School Completed");}
		if(p.getProperty("pg1_eduLevel","").equals("ED005")) {p.setProperty("pg1_eduLevel", "High School N/Completed");}
		if(p.getProperty("pg1_eduLevel","").equals("ED006")) {p.setProperty("pg1_eduLevel", "Grade School N/Completed");}

		if(p.getProperty("pg1_maritalStatus","").equals("UN")) {p.setProperty("pg1_maritalStatus", "");}
		if(p.getProperty("pg1_maritalStatus","").equals("M")) {p.setProperty("pg1_maritalStatus", "Married");}
		if(p.getProperty("pg1_maritalStatus","").equals("CL")) {p.setProperty("pg1_maritalStatus", "Common Law");}
		if(p.getProperty("pg1_maritalStatus","").equals("S")) {p.setProperty("pg1_maritalStatus", "Single");}
		if(p.getProperty("pg1_maritalStatus","").equals("DS")) {p.setProperty("pg1_maritalStatus", "Separated/Divorced");}

		//smoking amt
		if(p.getProperty("pg1_box3","").equals("LESS10")) {p.setProperty("pg1_box3", "<10");}
		if(p.getProperty("pg1_box3","").equals("UP20")) {p.setProperty("pg1_box3", "<20");}
		if(p.getProperty("pg1_box3","").equals("OVER20")) {p.setProperty("pg1_box3", ">20");}
		
		if(p.getProperty("pg1_ethnicBgMother","").equals("Unknown")) {p.setProperty("pg1_ethnicBgMother", "");}		
		if(p.getProperty("pg1_ethnicBgMother","").equals("UN")) {p.setProperty("pg1_ethnicBgMother", "");}
		if(p.getProperty("pg1_ethnicBgMother","").equals("ANC001")) {p.setProperty("pg1_ethnicBgMother", "Aboriginal");}
		if(p.getProperty("pg1_ethnicBgMother","").equals("ANC002")) {p.setProperty("pg1_ethnicBgMother", "Asian");}
		if(p.getProperty("pg1_ethnicBgMother","").equals("ANC005")) {p.setProperty("pg1_ethnicBgMother", "Black");}
		if(p.getProperty("pg1_ethnicBgMother","").equals("ANC007")) {p.setProperty("pg1_ethnicBgMother", "Caucasian");}
		if(p.getProperty("pg1_ethnicBgMother","").equals("OTHER")) {p.setProperty("pg1_ethnicBgMother", "Other");}
		
		if(p.getProperty("pg1_ethnicBgFather","").equals("Unknown")) {p.setProperty("pg1_ethnicBgFather", "");}		
		if(p.getProperty("pg1_ethnicBgFather","").equals("UN")) {p.setProperty("pg1_ethnicBgFather", "");}
		if(p.getProperty("pg1_ethnicBgFather","").equals("ANC001")) {p.setProperty("pg1_ethnicBgFather", "Aboriginal");}
		if(p.getProperty("pg1_ethnicBgFather","").equals("ANC002")) {p.setProperty("pg1_ethnicBgFather", "Asian");}
		if(p.getProperty("pg1_ethnicBgFather","").equals("ANC005")) {p.setProperty("pg1_ethnicBgFather", "Black");}
		if(p.getProperty("pg1_ethnicBgFather","").equals("ANC007")) {p.setProperty("pg1_ethnicBgFather", "Caucasian");}
		if(p.getProperty("pg1_ethnicBgFather","").equals("OTHER")) {p.setProperty("pg1_ethnicBgFather", "Other");}

		String ethnicM = p.getProperty("pg1_ethnicBgMother","");
		String ethnicF = p.getProperty("pg1_ethnicBgFather","");
		String ethnic = "";
		if(ethnicM.length() >0 && ethnicF.length()>0) {
			ethnic = ethnicM + " / " + ethnicF;
		}
		if(ethnicM.length() >0 && ethnicF.length() == 0) {
			ethnic = ethnicM + " / " + "Unknown";
		}
		if(ethnicM.length() == 0 && ethnicF.length() > 0) {
			ethnic = "Unknown" + " / " + ethnicF;
		}
		if(ethnicM.length() == 0 && ethnicF.length() ==0) {
			ethnic = "";
		}
		
		p.setProperty("pg1_ethnicBg",ethnic);
		
		
		if(p.getProperty("pg1_labRubella","").equals("NDONE")) {p.setProperty("pg1_labRubella", "");}
		if(p.getProperty("pg1_labRubella","").equals("Non-Immune")) {p.setProperty("pg1_labRubella", "Non-Imm.");}
		if(p.getProperty("pg1_labRubella","").equals("Immune")) {p.setProperty("pg1_labRubella", "Imm.");}
		if(p.getProperty("pg1_labRubella","").equals("Indeterminate")) {p.setProperty("pg1_labRubella", "Ind.");}


		if(p.getProperty("pg1_labHIV","").equals("NDONE")) {p.setProperty("pg1_labHIV", "");}
		if(p.getProperty("pg1_labHIV","").equals("POS")) {p.setProperty("pg1_labHIV", "+ve");}
		if(p.getProperty("pg1_labHIV","").equals("NEG")) {p.setProperty("pg1_labHIV", "-ve");}
		if(p.getProperty("pg1_labHIV","").equals("IND")) {p.setProperty("pg1_labHIV", "Ind.");}
		if(p.getProperty("pg1_labHIV","").equals("UNK")) {p.setProperty("pg1_labHIV", "Unknown");}

		if(p.getProperty("pg1_labRh","").equals("NDONE")) {p.setProperty("pg1_labRh", "");}
		if(p.getProperty("pg1_labRh","").equals("POS")) {p.setProperty("pg1_labRh", "POS");}
		if(p.getProperty("pg1_labRh","").equals("NEG")) {p.setProperty("pg1_labRh", "NEG");}
		if(p.getProperty("pg1_labRh","").equals("WPOS")) {p.setProperty("pg1_labRh", "Weak +ve.");}
		if(p.getProperty("pg1_labRh","").equals("UNK")) {p.setProperty("pg1_labRh", "Unknown");}
		
		if(p.getProperty("pg1_labGC","").equals("NDONE")) {p.setProperty("pg1_labGC", "");}
		if(p.getProperty("pg1_labChlamydia","").equals("NDONE")) {p.setProperty("pg1_labChlamydia", "");}
		if(p.getProperty("pg1_labHBsAg","").equals("NDONE")) {p.setProperty("pg1_labHBsAg", "");}
		if(p.getProperty("pg1_labVDRL","").equals("NDONE")) {p.setProperty("pg1_labVDRL", "");}
		if(p.getProperty("pg1_labSickle","").equals("NDONE")) {p.setProperty("pg1_labSickle", "");}
		
		if(p.getProperty("pg1_labABO","").equals("NDONE")) {p.setProperty("pg1_labABO", "");}
		
		
		return p;
	}
	
}
