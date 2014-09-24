	<%--

    Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
    This software is published under the GPL GNU General Public License.
    This program is free software; you can redistribute it and/or
    modify it under the terms of the GNU General Public License
    as published by the Free Software Foundation; either version 2
    of the License, or (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program; if not, write to the Free Software
    Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.

    This software was written for the
    Department of Family Medicine
    McMaster University
    Hamilton
    Ontario, Canada

--%>

<style>
	label {
		width: 125px;
	}
	.form-control-details {
		display: inline;
		width: 200px;
	}
	#photo {
		height: 150px;
		width: auto;
		cursor: pointer;
	}
</style>

<div class="col-lg-12" ng-hide="page.canRead">
	You have no right to access the data!
</div>
<div ng-show="page.canRead">
<div class="col-lg-8">
	<div class="alert alert-success" ng-show="page.saving">
		Saving...
	</div>
	
	<button type="button" class="btn {{needToSave()}}" ng-click="save()" ng-disabled="page.dataChanged<1">Save</button>
	<div class="btn-group">
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				Print <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a href="../report/GenerateEnvelopes.do?demos={{page.demo.demographicNo}}">PDF Envelope</a></li>
				<li><a ng-click="printLabel('PDFLabel')">PDF Label</a></li>
				<li><a ng-click="printLabel('PDFAddress')">PDF Address Label</a></li>
				<li><a ng-click="printLabel('PDFChart')">PDF Chart Label</a></li>
				<li><a ng-click="printLabel('PrintLabel')">Print Label</a></li>
				<li><a ng-click="printLabel('ClientLab')">Client Lab Label</a></li>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown" ng-show="page.integratorEnabled" style="color:{{page.integratorStatusColor}}" title="{{page.integratorStatusMsg}}">
				Integrator <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li ng-show="page.integratorOffline"><a style="color:#FF5500">{{page.integratorStatusMsg}}</a></li>
				<li ng-hide="page.integratorOffline" title="{{page.integratorStatusMsg}}"><a style="color:{{page.integratorStatusColor}}" ng-click="integratorDo('ViewCommunity')">View Integrated Community</a></li>
				<li><a ng-click="integratorDo('Linking')">Manage Linked Clients</a></li>
				<div ng-show="page.conformanceFeaturesEnabled && !page.integratorOffline">
					<li><a ng-click="integratorDo('Compare')">Compare with Integrator</a></li>
					<li><a ng-click="integratorDo('Update')">Update from Integrator</a></li>
					<li><a ng-click="integratorDo('SendNote')">Send Note to Integrator</a></li>
				</div>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				Appointment <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a ng-click="appointmentDo('ApptHistory')">Appointment History</a></li>
				<li><a ng-click="appointmentDo('WaitingList')">Waiting List</a></li>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				Billing <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a ng-click="billingDo('BillingHistory')">{{page.billingHistoryLabel}}</a></li>
				<li><a ng-click="billingDo('CreateInvoice')">Create Invoice</a></li>
				<li><a ng-click="billingDo('FluBilling')">Flu Billing</a></li>
				<li><a ng-click="billingDo('HospitalBilling')">Hospital Billing</a></li>
				<li><a ng-click="billingDo('AddBatchBilling')">Add Batch Billing</a></li>
				<li><a ng-click="billingDo('AddINR')">Add INR</a></li>
				<li><a ng-click="billingDo('BillINR')">Bill INR</a></li>
			</ul>
		</div>
		<div class="btn-group" ng-show="page.macPHRIdsSet">
			<button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown">
				Personal Health Record <span class="caret"></span>
			</button>
			<ul class="dropdown-menu">
				<li><a ng-click="macPHRDo('SendMessage')">Send Message to PHR</a></li>
				<li><a ng-click="macPHRDo('ViewRecord')">View PHR Record</a></li>
			</ul>
		</div>
		<div class="btn-group">
			<button type="button" class="btn btn-default" ng-click="exportDemographic()">Export</button>
		</div>
	</div>

	<button type="button" class="btn {{page.readyForSwipe}}" ng-show="page.workflowEnhance" ng-click="setSwipeReady()" title="Click for Card Swipe" style="padding-top: 0px; padding-bottom: 0px; font-size: small">
		{{page.swipecardMsg}}
	</button>
	<input type="text" id="swipecard" title="Click for Card Swipe" ng-model="page.swipecard" ng-show="page.workflowEnhance" ng-focus="setSwipeReady()" ng-blur="setSwipeReady('off')" ng-keypress="healthCardHandler($event.keyCode)" style="width:0px; border:none"/>

	<div id="pd1" ng-click="checkAction($event)" ng-keypress="checkAction($event)">
	<div class="form-group row">
		<fieldset>
			<legend>Demographic</legend>
		</fieldset>
		<div class="col-md-6">
			<label title="Required Field">Last Name <span style="color:red">*</span></label>
			<input type="text" class="form-control form-control-details" placeholder="Family Name" title="Family Name" ng-model="page.demo.lastName" ng-change="formatLastName()" style="background-color:{{page.lastNameColor}}"/>
		</div>
		<div class="col-md-6">
			<label title="Required Field">First Name <span style="color:red">*</span></label>
			<input type="text" class="form-control form-control-details" placeholder="First Name" title="First Name" ng-model="page.demo.firstName" ng-change="formatFirstName()" style="background-color:{{page.firstNameColor}}"/>
		</div>
		<div class="col-md-6">
			<label title="Required Field">Date of Birth <span style="color:red">*</span></label>
			<span style="white-space:nowrap">
				<input type="text" placeholder="YYYY" title="Birthday Year" class="form-control form-control-details" ng-model="page.demo.dobYear" ng-change="checkDate('DobY')" ng-blur="formatDate('DobY')" style="width:65px; background-color:{{page.dobYearColor}}" />
				<input type="text" placeholder="MM" title="Birthday Month" class="form-control form-control-details" ng-model="page.demo.dobMonth" ng-change="checkDate('DobM')" ng-blur="formatDate('DobM')" style="width:45px; background-color:{{page.dobMonthColor}}"/>
				<input type="text" placeholder="DD" title="Birthday Day" class="form-control form-control-details" ng-model="page.demo.dobDay" ng-change="checkDate('DobD')" ng-blur="formatDate('DobD')" style="width:45px; background-color:{{page.dobDayColor}}"/>
				({{page.demo.age}}y)
			</span>
		</div>
		<div class="col-md-6">
			<label title="Required Field">Sex <span style="color:red">*</span></label>
			<select class="form-control form-control-details" title="Sex" ng-model="page.demo.sex" style="background-color:{{page.sexColor}}">
				<option value="M">Male</option>
				<option value="F">Female</option>
				<option value="T">Transgendered</option>
				<option value="O">Other</option>
				<option value="U">Undefined</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Title</label>
			<select class="form-control form-control-details" title="Title" ng-model="page.demo.title">
				<option value="DR">DR</option>
				<option value="MS">MS</option>
				<option value="MISS">MISS</option>
				<option value="MRS">MRS</option>
				<option value="MR">MR</option>
				<option value="MSSR">MSSR</option>
				<option value="PROF">PROF</option>
				<option value="REEVE">REEVE</option>
				<option value="REV">REV</option>
				<option value="RT_HON">RT_HON</option>
				<option value="SEN">SEN</option>
				<option value="SGT">SGT</option>
				<option value="SR">SR</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>SIN #</label>
			<input type="text" class="form-control form-control-details" placeholder="SIN #" title="SIN #" ng-model="page.demo.sin"/>
		</div>
		<div class="col-md-6">
			<label>Language</label>
			<select class="form-control form-control-details" title="Language" ng-model="page.demo.officialLanguage">
				<option value="English">English</option>
				<option value="French">French</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Spoken</label>
			<select class="form-control form-control-details" title="Spoken Language" ng-model="page.demo.spokenLanguage">
				<option value="English" >English</option>
				<option value="French" >French</option>
				<option value="Abkhazian" >Abkhazian</option>
				<option value="Achinese" >Achinese</option>
				<option value="Acoli" >Acoli</option>
				<option value="Adangme" >Adangme</option>
				<option value="Adyghe" >Adyghe</option>
				<option value="Afar" >Afar</option>
				<option value="Afrihili" >Afrihili</option>
				<option value="Afrikaans" >Afrikaans</option>
				<option value="Afro-Asiatic (Other)" >Afro-Asiatic</option>
				<option value="Ainu" >Ainu</option>
				<option value="Akan" >Akan</option>
				<option value="Akkadian" >Akkadian</option>
				<option value="Albanian" >Albanian</option>
				<option value="Aleut" >Aleut</option>
				<option value="Algonquian Languages" >Algonquian Languages</option>
				<option value="Altaic (Other)" >Altaic</option>
				<option value="Amharic" >Amharic</option>
				<option value="Angika" >Angika</option>
				<option value="Apache Languages" >Apache Languages</option>
				<option value="Arabic" >Arabic</option>
				<option value="Aramaic" >Aramaic</option>
				<option value="Arapaho" >Arapaho</option>
				<option value="Araucanian" >Araucanian</option>
				<option value="Arawak" >Arawak</option>
				<option value="Argonese" >Argonese</option>
				<option value="Armenian" >Armenian</option>
				<option value="Aromanian" >Aromanian</option>
				<option value="Artificial (Other)" >Artificial</option>
				<option value="Assamese" >Assamese</option>
				<option value="Asturian" >Asturian</option>
				<option value="Athapascan Languages" >Athapascan Languages</option>
				<option value="Australian Languages" >Australian Languages</option>
				<option value="Austronesian (Other)" >Austronesian</option>
				<option value="Avaric" >Avaric</option>
				<option value="Avestan" >Avestan</option>
				<option value="Awadhi" >Awadhi</option>
				<option value="Aymara" >Aymara</option>
				<option value="Azerbaijani" >Azerbaijani</option>
				<option value="Balinese" >Balinese</option>
				<option value="Baltic (Other)" >Baltic</option>
				<option value="Baluchi" >Baluchi</option>
				<option value="Bambara" >Bambara</option>
				<option value="Bamileke Languages" >Bamileke Languages</option>
				<option value="Banda" >Banda</option>
				<option value="Bantu (Other)" >Bantu</option>
				<option value="Basa" >Basa</option>
				<option value="Bashkir" >Bashkir</option>
				<option value="Basque" >Basque</option>
				<option value="Batak (Indonesia)" >Batak (Indonesia)</option>
				<option value="Beja" >Beja</option>
				<option value="Belarusian" >Belarusian</option>
				<option value="Bemba" >Bemba</option>
				<option value="Bengali" >Bengali</option>
				<option value="Berber (Other)" >Berber</option>
				<option value="Bhojpuri" >Bhojpuri</option>
				<option value="Bihari" >Bihari</option>
				<option value="Bikol" >Bikol</option>
				<option value="Bini" >Bini</option>
				<option value="Bislama" >Bislama</option>
				<option value="Blin" >Blin</option>
				<option value="Bokmal, Norwegian" >Bokmal, Norwegian</option>
				<option value="Bosnian" >Bosnian</option>
				<option value="Braj" >Braj</option>
				<option value="Breton" >Breton</option>
				<option value="Buginese" >Buginese</option>
				<option value="Bulgarian" >Bulgarian</option>
				<option value="Buriat" >Buriat</option>
				<option value="Burmese" >Burmese</option>
				<option value="Caddo" >Caddo</option>
				<option value="Cantonese" >Cantonese</option>
				<option value="Carib" >Carib</option>
				<option value="Catalan" >Catalan</option>
				<option value="Caucasian (Other)" >Caucasian</option>
				<option value="Cebuano" >Cebuano</option>
				<option value="Celtic (Other)" >Celtic</option>
				<option value="Central American Indian (Other)" >Central American Indian</option>
				<option value="Chagatai" >Chagatai</option>
				<option value="Chamic Languages" >Chamic Languages</option>
				<option value="Chamorro" >Chamorro</option>
				<option value="Chechen" >Chechen</option>
				<option value="Cherokee" >Cherokee</option>
				<option value="Cheyenne" >Cheyenne</option>
				<option value="Chibcha" >Chibcha</option>
				<option value="Chichewa" >Chichewa</option>
				<option value="Chinese" >Chinese</option>
				<option value="Chinook Jargon" >Chinook Jargon</option>
				<option value="Chipewyan" >Chipewyan</option>
				<option value="Choctaw" >Choctaw</option>
				<option value="Chuukese" >Chuukese</option>
				<option value="Chuvash" >Chuvash</option>
				<option value="Classical Nepal Bhasa" >Classical Nepal Bhasa</option>
				<option value="Coptic" >Coptic</option>
				<option value="Cornish" >Cornish</option>
				<option value="Corsican" >Corsican</option>
				<option value="Cree" >Cree</option>
				<option value="Creek" >Creek</option>
				<option value="Creoles And Pidgins (Other)" >Creoles & Pidgins</option>
				<option value="Creoles And Pidgins, English-Based (Other)" >Creoles & Pidgins, ENG-Based</option>
				<option value="Creoles And Pidgins, French-Based (Other)" >Creoles & Pidgins, FRE-Based</option>
				<option value="Creoles And Pidgins, Portuguese-Based (Other)" >Creoles & Pidgins, POR-Based</option>
				<option value="Crimean Tatar" >Crimean Tatar</option>
				<option value="Croatian" >Croatian</option>
				<option value="Cushitic (Other)" >Cushitic</option>
				<option value="Czech" >Czech</option>
				<option value="Dakota" >Dakota</option>
				<option value="Danish" >Danish</option>
				<option value="Dargwa" >Dargwa</option>
				<option value="Dayak" >Dayak</option>
				<option value="Delaware" >Delaware</option>
				<option value="Dhivehi" >Dhivehi</option>
				<option value="Dinka" >Dinka</option>
				<option value="Dogri" >Dogri</option>
				<option value="Dogrib" >Dogrib</option>
				<option value="Dravidian (Other)" >Dravidian</option>
				<option value="Duala" >Duala</option>
				<option value="Dutch" >Dutch</option>
				<option value="Dyula" >Dyula</option>
				<option value="Dzongkha" >Dzongkha</option>
				<option value="Eastern Frisian" >Eastern Frisian</option>
				<option value="Efik" >Efik</option>
				<option value="Egyptian (Ancient)" >Egyptian (Ancient)</option>
				<option value="Ekajuk" >Ekajuk</option>
				<option value="Elamite" >Elamite</option>
				<option value="Erzya" >Erzya</option>
				<option value="Esperanto" >Esperanto</option>
				<option value="Estonian" >Estonian</option>
				<option value="Ewe" >Ewe</option>
				<option value="Ewondo" >Ewondo</option>
				<option value="Fang" >Fang</option>
				<option value="Fanti" >Fanti</option>
				<option value="Faroese" >Faroese</option>
				<option value="Fijian" >Fijian</option>
				<option value="Filipino; Pilipino" >Filipino; Pilipino</option>
				<option value="Finnish" >Finnish</option>
				<option value="Finno-Ugrian (Other)" >Finno-Ugrian</option>
				<option value="Fon" >Fon</option>
				<option value="Friulian" >Friulian</option>
				<option value="Fulah" >Fulah</option>
				<option value="Ga" >Ga</option>
				<option value="Gaelic" >Gaelic</option>
				<option value="Gallegan" >Gallegan</option>
				<option value="Ganda" >Ganda</option>
				<option value="Gayo" >Gayo</option>
				<option value="Gbaya" >Gbaya</option>
				<option value="Geez" >Geez</option>
				<option value="Georgian" >Georgian</option>
				<option value="German" >German</option>
				<option value="Germanic (Other)" >Germanic</option>
				<option value="Gikuyu" >Gikuyu</option>
				<option value="Gilbertese" >Gilbertese</option>
				<option value="Gondi" >Gondi</option>
				<option value="Gorontalo" >Gorontalo</option>
				<option value="Gothic" >Gothic</option>
				<option value="Grebo" >Grebo</option>
				<option value="Greek, Modern (1453-)" >Greek, Modern (1453-)</option>
				<option value="Guarani" >Guarani</option>
				<option value="Gujarati" >Gujarati</option>
				<option value="Gwich'in" >Gwich'in</option>
				<option value="Haida" >Haida</option>
				<option value="Haitian Creole" >Haitian Creole</option>
				<option value="Hausa" >Hausa</option>
				<option value="Hawaiian" >Hawaiian</option>
				<option value="Hebrew" >Hebrew</option>
				<option value="Herero" >Herero</option>
				<option value="Hiligaynon" >Hiligaynon</option>
				<option value="Himachali" >Himachali</option>
				<option value="Hindi" >Hindi</option>
				<option value="Hiri Motu" >Hiri Motu</option>
				<option value="Hittite" >Hittite</option>
				<option value="Hmong" >Hmong</option>
				<option value="Hungarian" >Hungarian</option>
				<option value="Hupa" >Hupa</option>
				<option value="Iban" >Iban</option>
				<option value="Icelandic" >Icelandic</option>
				<option value="Ido" >Ido</option>
				<option value="Igbo" >Igbo</option>
				<option value="Ijo" >Ijo</option>
				<option value="Iloko" >Iloko</option>
				<option value="Inari Sami" >Inari Sami</option>
				<option value="Indic (Other)" >Indic</option>
				<option value="Indo-European (Other)" >Indo-European</option>
				<option value="Indonesian" >Indonesian</option>
				<option value="Ingush" >Ingush</option>
				<option value="Interlingua (International Auxiliary Lang. Assoc.)" >Interlingua (IALA)</option>
				<option value="Interlingue" >Interlingue</option>
				<option value="Inuktitut" >Inuktitut</option>
				<option value="Inupiaq" >Inupiaq</option>
				<option value="Iranian (Other)" >Iranian</option>
				<option value="Irish" >Irish</option>
				<option value="Iroquoian Languages" >Iroquoian Languages</option>
				<option value="Italian" >Italian</option>
				<option value="Japanese" >Japanese</option>
				<option value="Javanese" >Javanese</option>
				<option value="Judeo-Arabic" >Judeo-Arabic</option>
				<option value="Judeo-Persian" >Judeo-Persian</option>
				<option value="Kabardian" >Kabardian</option>
				<option value="Kabyle" >Kabyle</option>
				<option value="Kachin" >Kachin</option>
				<option value="Kalaallisut" >Kalaallisut</option>
				<option value="Kalmyk" >Kalmyk</option>
				<option value="Kamba" >Kamba</option>
				<option value="Kannada" >Kannada</option>
				<option value="Kanuri" >Kanuri</option>
				<option value="Kara-Kalpak" >Kara-Kalpak</option>
				<option value="Karachay-Balkar" >Karachay-Balkar</option>
				<option value="Karelian" >Karelian</option>
				<option value="Karen" >Karen</option>
				<option value="Kashmiri" >Kashmiri</option>
				<option value="Kashubian" >Kashubian</option>
				<option value="Kawi" >Kawi</option>
				<option value="Kazakh" >Kazakh</option>
				<option value="Khasi" >Khasi</option>
				<option value="Khmer" >Khmer</option>
				<option value="Khoisan (Other)" >Khoisan</option>
				<option value="Khotanese" >Khotanese</option>
				<option value="Kimbundu" >Kimbundu</option>
				<option value="Kinyarwanda" >Kinyarwanda</option>
				<option value="Kirghiz" >Kirghiz</option>
				<option value="Klingon" >Klingon</option>
				<option value="Komi" >Komi</option>
				<option value="Kongo" >Kongo</option>
				<option value="Konkani" >Konkani</option>
				<option value="Korean" >Korean</option>
				<option value="Kosraean" >Kosraean</option>
				<option value="Kpelle" >Kpelle</option>
				<option value="Kru" >Kru</option>
				<option value="Kumyk" >Kumyk</option>
				<option value="Kurdish" >Kurdish</option>
				<option value="Kurukh" >Kurukh</option>
				<option value="Kutenai" >Kutenai</option>
				<option value="Kwanyama" >Kwanyama</option>
				<option value="Ladino" >Ladino</option>
				<option value="Lahnda" >Lahnda</option>
				<option value="Lamba" >Lamba</option>
				<option value="Lao" >Lao</option>
				<option value="Latin" >Latin</option>
				<option value="Latvian" >Latvian</option>
				<option value="Lezghian" >Lezghian</option>
				<option value="Limburgish" >Limburgish</option>
				<option value="Lingala" >Lingala</option>
				<option value="Lithuanian" >Lithuanian</option>
				<option value="Lojban" >Lojban</option>
				<option value="Low German" >Low German</option>
				<option value="Lower Sorbian" >Lower Sorbian</option>
				<option value="Lozi" >Lozi</option>
				<option value="Luba-Katanga" >Luba-Katanga</option>
				<option value="Luba-Lulua" >Luba-Lulua</option>
				<option value="Luiseno" >Luiseno</option>
				<option value="Lule Sami" >Lule Sami</option>
				<option value="Lunda" >Lunda</option>
				<option value="Luo (Kenya And Tanzania)" >Luo (Kenya & Tanzania)</option>
				<option value="Lushai" >Lushai</option>
				<option value="Luxembourgish" >Luxembourgish</option>
				<option value="Macedonian" >Macedonian</option>
				<option value="Madurese" >Madurese</option>
				<option value="Magahi" >Magahi</option>
				<option value="Maithili" >Maithili</option>
				<option value="Makasar" >Makasar</option>
				<option value="Malagasy" >Malagasy</option>
				<option value="Malay" >Malay</option>
				<option value="Malayalam" >Malayalam</option>
				<option value="Maltese" >Maltese</option>
				<option value="Manchu" >Manchu</option>
				<option value="Mandar" >Mandar</option>
				<option value="Mandarin" >Mandarin</option>
				<option value="Mandingo" >Mandingo</option>
				<option value="Manipuri" >Manipuri</option>
				<option value="Manobo Languages" >Manobo Languages</option>
				<option value="Manx" >Manx</option>
				<option value="Maori" >Maori</option>
				<option value="Marathi" >Marathi</option>
				<option value="Mari" >Mari</option>
				<option value="Marshall" >Marshall</option>
				<option value="Marwari" >Marwari</option>
				<option value="Masai" >Masai</option>
				<option value="Mayan Languages" >Mayan Languages</option>
				<option value="Mende" >Mende</option>
				<option value="Micmac" >Micmac</option>
				<option value="Minangkabau" >Minangkabau</option>
				<option value="Mirandese" >Mirandese</option>
				<option value="Miscellaneous Languages" >Miscellaneous Languages</option>
				<option value="Mohawk" >Mohawk</option>
				<option value="Moksha" >Moksha</option>
				<option value="Moldavian" >Moldavian</option>
				<option value="Mon-Khmer (Other)" >Mon-Khmer</option>
				<option value="Mongo" >Mongo</option>
				<option value="Mongolian" >Mongolian</option>
				<option value="Mossi" >Mossi</option>
				<option value="Multiple Languages" >Multiple Languages</option>
				<option value="Munda Languages" >Munda Languages</option>
				<option value="N'Ko" >N'Ko</option>
				<option value="Nahuatl" >Nahuatl</option>
				<option value="Nauru" >Nauru</option>
				<option value="Navajo" >Navajo</option>
				<option value="Ndonga" >Ndonga</option>
				<option value="Neapolitan" >Neapolitan</option>
				<option value="Nepal Bhasa" >Nepal Bhasa</option>
				<option value="Nepali" >Nepali</option>
				<option value="Nias" >Nias</option>
				<option value="Niger-Kordofanian (Other)" >Niger-Kordofanian</option>
				<option value="Nilo-Saharan (Other)" >Nilo-Saharan</option>
				<option value="Niuean" >Niuean</option>
				<option value="Nogai" >Nogai</option>
				<option value="Norse, Old" >Norse, Old</option>
				<option value="North American Indian (Other)" >North American Indian</option>
				<option value="North Ndebele" >North Ndebele</option>
				<option value="Northern Frisian" >Northern Frisian</option>
				<option value="Northern Sami" >Northern Sami</option>
				<option value="Northern Sotho" >Northern Sotho</option>
				<option value="Norwegian" >Norwegian</option>
				<option value="Norwegian Nynorsk" >Norwegian Nynorsk</option>
				<option value="Nubian Languages" >Nubian Languages</option>
				<option value="Nyamwezi" >Nyamwezi</option>
				<option value="Nyankole" >Nyankole</option>
				<option value="Nyoro" >Nyoro</option>
				<option value="Nzima" >Nzima</option>
				<option value="Occitan (Post 1500)" >Occitan (Post 1500)</option>
				<option value="Ojibwa" >Ojibwa</option>
				<option value="Old Church Slavonic" >Old Church Slavonic</option>
				<option value="Oriya" >Oriya</option>
				<option value="Oromo" >Oromo</option>
				<option value="Osage" >Osage</option>
				<option value="Ossetic" >Ossetic</option>
				<option value="Otomian Languages" >Otomian Languages</option>
				<option value="Pahlavi" >Pahlavi</option>
				<option value="Palauan" >Palauan</option>
				<option value="Pali" >Pali</option>
				<option value="Pampanga" >Pampanga</option>
				<option value="Pangasinan" >Pangasinan</option>
				<option value="Papiamento" >Papiamento</option>
				<option value="Papuan (Other)" >Papuan</option>
				<option value="Persian" >Persian</option>
				<option value="Philippine (Other)" >Philippine</option>
				<option value="Phoenician" >Phoenician</option>
				<option value="Pohnpeian" >Pohnpeian</option>
				<option value="Polish" >Polish</option>
				<option value="Portuguese" >Portuguese</option>
				<option value="Prakrit Languages" >Prakrit Languages</option>
				<option value="Punjabi" >Punjabi</option>
				<option value="Pushto" >Pushto</option>
				<option value="Quechua" >Quechua</option>
				<option value="Raeto-Romance" >Raeto-Romance</option>
				<option value="Rajasthani" >Rajasthani</option>
				<option value="Rapanui" >Rapanui</option>
				<option value="Rarotongan" >Rarotongan</option>
				<option value="Romance (Other)" >Romance</option>
				<option value="Romanian" >Romanian</option>
				<option value="Romany" >Romany</option>
				<option value="Rundi" >Rundi</option>
				<option value="Russian" >Russian</option>
				<option value="Salishan Languages" >Salishan Languages</option>
				<option value="Samaritan Aramaic" >Samaritan Aramaic</option>
				<option value="Sami Languages" >Sami Languages</option>
				<option value="Samoan" >Samoan</option>
				<option value="Sandawe" >Sandawe</option>
				<option value="Sango" >Sango</option>
				<option value="Sanskrit" >Sanskrit</option>
				<option value="Santali" >Santali</option>
				<option value="Sardinian" >Sardinian</option>
				<option value="Sasak" >Sasak</option>
				<option value="Scots" >Scots</option>
				<option value="Selkup" >Selkup</option>
				<option value="Semitic (Other)" >Semitic</option>
				<option value="Serbian" >Serbian</option>
				<option value="Serer" >Serer</option>
				<option value="Shan" >Shan</option>
				<option value="Shona" >Shona</option>
				<option value="Sichuan Yi" >Sichuan Yi</option>
				<option value="Sicilian" >Sicilian</option>
				<option value="Sidamo" >Sidamo</option>
				<option value="Sign Languages" >Sign Languages</option>
				<option value="Siksika" >Siksika</option>
				<option value="Sindhi" >Sindhi</option>
				<option value="Sinhalese" >Sinhalese</option>
				<option value="Sino-Tibetan (Other)" >Sino-Tibetan</option>
				<option value="Siouan Languages" >Siouan Languages</option>
				<option value="Skolt Sami" >Skolt Sami</option>
				<option value="Slave (Athapascan)" >Slave (Athapascan)</option>
				<option value="Slavic (Other)" >Slavic</option>
				<option value="Slovak" >Slovak</option>
				<option value="Slovenian" >Slovenian</option>
				<option value="Sogdian" >Sogdian</option>
				<option value="Somali" >Somali</option>
				<option value="Songhai" >Songhai</option>
				<option value="Soninke" >Soninke</option>
				<option value="Sorbian Languages" >Sorbian Languages</option>
				<option value="Sotho, Southern" >Sotho, Southern</option>
				<option value="South American Indian (Other)" >South American Indian</option>
				<option value="South Ndebele" >South Ndebele</option>
				<option value="Southern Altai" >Southern Altai</option>
				<option value="Southern Sami" >Southern Sami</option>
				<option value="Spanish; Castilian" >Spanish; Castilian</option>
				<option value="Sranan Togo" >Sranan Togo</option>
				<option value="Sukuma" >Sukuma</option>
				<option value="Sumerian" >Sumerian</option>
				<option value="Sundanese" >Sundanese</option>
				<option value="Susu" >Susu</option>
				<option value="Swahili" >Swahili</option>
				<option value="Swati" >Swati</option>
				<option value="Swedish" >Swedish</option>
				<option value="Swiss German" >Swiss German</option>
				<option value="Syriac" >Syriac</option>
				<option value="Tagalog" >Tagalog</option>
				<option value="Tahitian" >Tahitian</option>
				<option value="Tai (Other)" >Tai</option>
				<option value="Tajik" >Tajik</option>
				<option value="Tamashek" >Tamashek</option>
				<option value="Tamil" >Tamil</option>
				<option value="Tatar" >Tatar</option>
				<option value="Telugu" >Telugu</option>
				<option value="Tereno" >Tereno</option>
				<option value="Tetum" >Tetum</option>
				<option value="Thai" >Thai</option>
				<option value="Tibetan" >Tibetan</option>
				<option value="Tigre" >Tigre</option>
				<option value="Tigrinya" >Tigrinya</option>
				<option value="Timne" >Timne</option>
				<option value="Tiv" >Tiv</option>
				<option value="Tlingit" >Tlingit</option>
				<option value="Tok Pisin" >Tok Pisin</option>
				<option value="Tokelau" >Tokelau</option>
				<option value="Tonga (Nyasa)" >Tonga (Nyasa)</option>
				<option value="Tonga (Tonga Islands)" >Tonga (Tonga Islands)</option>
				<option value="Tsimshian" >Tsimshian</option>
				<option value="Tsonga" >Tsonga</option>
				<option value="Tswana" >Tswana</option>
				<option value="Tumbuka" >Tumbuka</option>
				<option value="Tupi Languages" >Tupi Languages</option>
				<option value="Turkish" >Turkish</option>
				<option value="Turkmen" >Turkmen</option>
				<option value="Tuvalu" >Tuvalu</option>
				<option value="Tuvinian" >Tuvinian</option>
				<option value="Twi" >Twi</option>
				<option value="Udmurt" >Udmurt</option>
				<option value="Ugaritic" >Ugaritic</option>
				<option value="Ukrainian" >Ukrainian</option>
				<option value="Umbundu" >Umbundu</option>
				<option value="Undetermined" >Undetermined</option>
				<option value="Upper Sorbian" >Upper Sorbian</option>
				<option value="Urdu" >Urdu</option>
				<option value="Uyghur" >Uyghur</option>
				<option value="Uzbek" >Uzbek</option>
				<option value="Vai" >Vai</option>
				<option value="Venda" >Venda</option>
				<option value="Vietnamese" >Vietnamese</option>
				<option value="Volapuk" >Volapuk</option>
				<option value="Votic" >Votic</option>
				<option value="Wakashan Languages" >Wakashan Languages</option>
				<option value="Walamo" >Walamo</option>
				<option value="Walloon" >Walloon</option>
				<option value="Waray" >Waray</option>
				<option value="Washo" >Washo</option>
				<option value="Welsh" >Welsh</option>
				<option value="Western Frisian" >Western Frisian</option>
				<option value="Wolof" >Wolof</option>
				<option value="Xhosa" >Xhosa</option>
				<option value="Yakut" >Yakut</option>
				<option value="Yao" >Yao</option>
				<option value="Yapese" >Yapese</option>
				<option value="Yiddish" >Yiddish</option>
				<option value="Yoruba" >Yoruba</option>
				<option value="Yupik Languages" >Yupik Languages</option>
				<option value="Zande" >Zande</option>
				<option value="Zapotec" >Zapotec</option>
				<option value="Zazaki" >Zazaki</option>
				<option value="Zenaga" >Zenaga</option>
				<option value="Zhuang" >Zhuang</option>
				<option value="Zulu" >Zulu</option>
				<option value="Zuni" >Zuni</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Aboriginal</label>
			<select class="form-control form-control-details" title="Aboriginal" ng-model="page.aboriginal.value">
				<option value="Yes">Yes</option>
				<option value="No">No</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>MyOSCAR</label>
			<input type="text" class="form-control form-control-details" placeholder="MyOSCAR UserName" title="MyOSCAR UserName" ng-model="page.demo.myOscarUserName"/>
		</div>
		<div class="col-md-6"></div>
		<div class="col-md-6">
			<label/>
			<button type="button" class="btn" style="padding-top: 0px; padding-bottom: 0px; font-size: small" ng-click="macPHRDo('Register')" ng-show="page.demo.myOscarUserName==null || page.demo.myOscarUserName==''">Register for MyOSCAR</button>
		</div>
		<div class="col-md-11">
			<label>Country of Origin</label>
			<select class="form-control form-control-details" title="Country of Origin" ng-model="page.demo.countryOfOrigin" style="width:520px">
				<option value="AF">AFGHANISTAN</option>
				<option value="AX">ALAND ISLANDS</option>
				<option value="AL">ALBANIA</option>
				<option value="DZ">ALGERIA</option>
				<option value="AS">AMERICAN SAMOA</option>
				<option value="AD">ANDORRA</option>
				<option value="AO">ANGOLA</option>
				<option value="AI">ANGUILLA</option>
				<option value="AQ">ANTARCTICA</option>
				<option value="AG">ANTIGUA AND BARBUDA</option>
				<option value="AR">ARGENTINA</option>
				<option value="AM">ARMENIA</option>
				<option value="AW">ARUBA</option>
				<option value="AU">AUSTRALIA</option>
				<option value="AT">AUSTRIA</option>
				<option value="AZ">AZERBAIJAN</option>
				<option value="BS">BAHAMAS</option>
				<option value="BH">BAHRAIN</option>
				<option value="BD">BANGLADESH</option>
				<option value="BB">BARBADOS</option>
				<option value="BY">BELARUS</option>
				<option value="BE">BELGIUM</option>
				<option value="BZ">BELIZE</option>
				<option value="BJ">BENIN</option>
				<option value="BM">BERMUDA</option>
				<option value="BT">BHUTAN</option>
				<option value="BO">BOLIVIA</option>
				<option value="BA">BOSNIA AND HERZEGOVINA</option>
				<option value="BW">BOTSWANA</option>
				<option value="BV">BOUVET ISLAND</option>
				<option value="BR">BRAZIL</option>
				<option value="IO">BRITISH INDIAN OCEAN TERRITORY</option>
				<option value="BN">BRUNEI DARUSSALAM</option>
				<option value="BG">BULGARIA</option>
				<option value="BF">BURKINA FASO</option>
				<option value="BI">BURUNDI</option>
				<option value="KH">CAMBODIA</option>
				<option value="CM">CAMEROON</option>
				<option value="CA">CANADA</option>
				<option value="CV">CAPE VERDE</option>
				<option value="KY">CAYMAN ISLANDS</option>
				<option value="CF">CENTRAL AFRICAN REPUBLIC</option>
				<option value="TD">CHAD</option>
				<option value="CL">CHILE</option>
				<option value="CN">CHINA</option>
				<option value="CX">CHRISTMAS ISLAND</option>
				<option value="CC">COCOS (KEELING) ISLANDS</option>
				<option value="CO">COLOMBIA</option>
				<option value="KM">COMOROS</option>
				<option value="CG">CONGO</option>
				<option value="CD">CONGO, THE DEMOCRATIC REPUBLIC OF THE</option>
				<option value="CK">COOK ISLANDS</option>
				<option value="CR">COSTA RICA</option>
				<option value="CI">CïTE D IVOIRE</option>
				<option value="HR">CROATIA</option>
				<option value="CU">CUBA</option>
				<option value="CY">CYPRUS</option>
				<option value="CZ">CZECH REPUBLIC</option>
				<option value="DK">DENMARK</option>
				<option value="DJ">DJIBOUTI</option>
				<option value="DM">DOMINICA</option>
				<option value="DO">DOMINICAN REPUBLIC</option>
				<option value="EC">ECUADOR</option>
				<option value="EG">EGYPT</option>
				<option value="SV">EL SALVADOR</option>
				<option value="GQ">EQUATORIAL GUINEA</option>
				<option value="ER">ERITREA</option>
				<option value="EE">ESTONIA</option>
				<option value="ET">ETHIOPIA</option>
				<option value="FK">FALKLAND ISLANDS (MALVINAS)</option>
				<option value="FO">FAROE ISLANDS</option>
				<option value="FJ">FIJI</option>
				<option value="FI">FINLAND</option>
				<option value="FR">FRANCE</option>
				<option value="GF">FRENCH GUIANA</option>
				<option value="PF">FRENCH POLYNESIA</option>
				<option value="TF">FRENCH SOUTHERN TERRITORIES</option>
				<option value="GA">GABON</option>
				<option value="GM">GAMBIA</option>
				<option value="GE">GEORGIA</option>
				<option value="DE">GERMANY</option>
				<option value="GH">GHANA</option>
				<option value="GI">GIBRALTAR</option>
				<option value="GR">GREECE</option>
				<option value="GL">GREENLAND</option>
				<option value="GD">GRENADA</option>
				<option value="GP">GUADELOUPE</option>
				<option value="GU">GUAM</option>
				<option value="GT">GUATEMALA</option>
				<option value="GG">GUERNSEY</option>
				<option value="GN">GUINEA</option>
				<option value="GW">GUINEA-BISSAU</option>
				<option value="GY">GUYANA</option>
				<option value="HT">HAITI</option>
				<option value="HM">HEARD ISLAND AND MCDONALD ISLANDS</option>
				<option value="VA">HOLY SEE (VATICAN CITY STATE)</option>
				<option value="HN">HONDURAS</option>
				<option value="HK">HONG KONG</option>
				<option value="HU">HUNGARY</option>
				<option value="IS">ICELAND</option>
				<option value="IN">INDIA</option>
				<option value="ID">INDONESIA</option>
				<option value="IR">IRAN, ISLAMIC REPUBLIC OF</option>
				<option value="IQ">IRAQ</option>
				<option value="IE">IRELAND</option>
				<option value="IM">ISLE OF MAN</option>
				<option value="IL">ISRAEL</option>
				<option value="IT">ITALY</option>
				<option value="JM">JAMAICA</option>
				<option value="JP">JAPAN</option>
				<option value="JE">JERSEY</option>
				<option value="JO">JORDAN</option>
				<option value="KZ">KAZAKHSTAN</option>
				<option value="KE">KENYA</option>
				<option value="KI">KIRIBATI</option>
				<option value="KP">KOREA, DEMOCRATIC PEOPLES REPUBLIC OF</option>
				<option value="KR">KOREA, REPUBLIC OF</option>
				<option value="KW">KUWAIT</option>
				<option value="KG">KYRGYZSTAN</option>
				<option value="LA">LAO PEOPLES DEMOCRATIC REPUBLIC</option>
				<option value="LV">LATVIA</option>
				<option value="LB">LEBANON</option>
				<option value="LS">LESOTHO</option>
				<option value="LR">LIBERIA</option>
				<option value="LY">LIBYAN ARAB JAMAHIRIYA</option>
				<option value="LI">LIECHTENSTEIN</option>
				<option value="LT">LITHUANIA</option>
				<option value="LU">LUXEMBOURG</option>
				<option value="MO">MACAO</option>
				<option value="MK">MACEDONIA, THE FORMER YUGOSLAV REPUBLIC OF</option>
				<option value="MG">MADAGASCAR</option>
				<option value="MW">MALAWI</option>
				<option value="MY">MALAYSIA</option>
				<option value="MV">MALDIVES</option>
				<option value="ML">MALI</option>
				<option value="MT">MALTA</option>
				<option value="MH">MARSHALL ISLANDS</option>
				<option value="MQ">MARTINIQUE</option>
				<option value="MR">MAURITANIA</option>
				<option value="MU">MAURITIUS</option>
				<option value="YT">MAYOTTE</option>
				<option value="MX">MEXICO</option>
				<option value="FM">MICRONESIA, FEDERATED STATES OF</option>
				<option value="MD">MOLDOVA</option>
				<option value="MC">MONACO</option>
				<option value="MN">MONGOLIA</option>
				<option value="ME">MONTENEGRO</option>
				<option value="MS">MONTSERRAT</option>
				<option value="MA">MOROCCO</option>
				<option value="MZ">MOZAMBIQUE</option>
				<option value="MM">MYANMAR</option>
				<option value="NA">NAMIBIA</option>
				<option value="NR">NAURU</option>
				<option value="NP">NEPAL</option>
				<option value="NL">NETHERLANDS</option>
				<option value="AN">NETHERLANDS ANTILLES</option>
				<option value="NC">NEW CALEDONIA</option>
				<option value="NZ">NEW ZEALAND</option>
				<option value="NI">NICARAGUA</option>
				<option value="NE">NIGER</option>
				<option value="NG">NIGERIA</option>
				<option value="NU">NIUE</option>
				<option value="NF">NORFOLK ISLAND</option>
				<option value="MP">NORTHERN MARIANA ISLANDS</option>
				<option value="NO">NORWAY</option>
				<option value="OM">OMAN</option>
				<option value="PK">PAKISTAN</option>
				<option value="PW">PALAU</option>
				<option value="PS">PALESTINIAN TERRITORY, OCCUPIED</option>
				<option value="PA">PANAMA</option>
				<option value="PG">PAPUA NEW GUINEA</option>
				<option value="PY">PARAGUAY</option>
				<option value="PE">PERU</option>
				<option value="PH">PHILIPPINES</option>
				<option value="PN">PITCAIRN</option>
				<option value="PL">POLAND</option>
				<option value="PT">PORTUGAL</option>
				<option value="PR">PUERTO RICO</option>
				<option value="QA">QATAR</option>
				<option value="RE">RƒUNION</option>
				<option value="RO">ROMANIA</option>
				<option value="RU">RUSSIAN FEDERATION</option>
				<option value="RW">RWANDA</option>
				<option value="BL">SAINT BARTHƒLEMY</option>
				<option value="SH">SAINT HELENA</option>
				<option value="KN">SAINT KITTS AND NEVIS</option>
				<option value="LC">SAINT LUCIA</option>
				<option value="MF">SAINT MARTIN</option>
				<option value="PM">SAINT PIERRE AND MIQUELON</option>
				<option value="VC">SAINT VINCENT AND THE GRENADINES</option>
				<option value="WS">SAMOA</option>
				<option value="SM">SAN MARINO</option>
				<option value="ST">SAO TOME AND PRINCIPE</option>
				<option value="SA">SAUDI ARABIA</option>
				<option value="SN">SENEGAL</option>
				<option value="RS">SERBIA</option>
				<option value="SC">SEYCHELLES</option>
				<option value="SL">SIERRA LEONE</option>
				<option value="SG">SINGAPORE</option>
				<option value="SK">SLOVAKIA</option>
				<option value="SI">SLOVENIA</option>
				<option value="SB">SOLOMON ISLANDS</option>
				<option value="SO">SOMALIA</option>
				<option value="ZA">SOUTH AFRICA</option>
				<option value="GS">SOUTH GEORGIA AND THE SOUTH SANDWICH ISLANDS</option>
				<option value="ES">SPAIN</option>
				<option value="LK">SRI LANKA</option>
				<option value="SD">SUDAN</option>
				<option value="SR">SURINAME</option>
				<option value="SJ">SVALBARD AND JAN MAYEN</option>
				<option value="SZ">SWAZILAND</option>
				<option value="SE">SWEDEN</option>
				<option value="CH">SWITZERLAND</option>
				<option value="SY">SYRIAN ARAB REPUBLIC</option>
				<option value="TW">TAIWAN, PROVINCE OF CHINA</option>
				<option value="TJ">TAJIKISTAN</option>
				<option value="TZ">TANZANIA, UNITED REPUBLIC OF</option>
				<option value="TH">THAILAND</option>
				<option value="TL">TIMOR-LESTE</option>
				<option value="TG">TOGO</option>
				<option value="TK">TOKELAU</option>
				<option value="TO">TONGA</option>
				<option value="TT">TRINIDAD AND TOBAGO</option>
				<option value="TN">TUNISIA</option>
				<option value="TR">TURKEY</option>
				<option value="TM">TURKMENISTAN</option>
				<option value="TC">TURKS AND CAICOS ISLANDS</option>
				<option value="TV">TUVALU</option>
				<option value="UG">UGANDA</option>
				<option value="UA">UKRAINE</option>
				<option value="AE">UNITED ARAB EMIRATES</option>
				<option value="GB">UNITED KINGDOM</option>
				<option value="US">UNITED STATES</option>
				<option value="UM">UNITED STATES MINOR OUTLYING ISLANDS</option>
				<option value="UY">URUGUAY</option>
				<option value="UZ">UZBEKISTAN</option>
				<option value="VU">VANUATU</option>
				<option value="VA">VATICAN CITY STATE</option>
				<option value="VE">VENEZUELA</option>
				<option value="VN">VIET NAM</option>
				<option value="VG">VIRGIN ISLANDS, BRITISH</option>
				<option value="VI">VIRGIN ISLANDS, U.S.</option>
				<option value="WF">WALLIS AND FUTUNA</option>
				<option value="EH">WESTERN SAHARA</option>
				<option value="YE">YEMEN</option>
				<option value="ZM">ZAMBIA</option>
				<option value="ZW">ZIMBABWE</option>		
			</select>
		</div>
	</div>
 
	<div class="form-group row">
		<fieldset>
			<legend>Contact Information</legend>
		</fieldset>
		<div class="col-md-6">
		<label>Address</label>
		<input type="text" placeholder="Address" title="Address" class="form-control form-control-details" ng-model="page.demo.address.address"/>
		</div>
		<div class="col-md-6">
			<label>City</label>
			<input type="text" class="form-control form-control-details" placeholder="City" title="City" ng-model="page.demo.address.city"/>
		</div>
		<div class="col-md-6">
			<label>Province</label>
			<select class="form-control form-control-details" title="Province" ng-model="page.demo.address.province">
				<option value="AB" >AB-Alberta</option>
				<option value="BC" >BC-British Columbia</option>
				<option value="MB" >MB-Manitoba</option>
				<option value="NB" >NB-New Brunswick</option>
				<option value="NL" >NL-Newfoundland Labrador</option>
				<option value="NT" >NT-Northwest Territory</option>
				<option value="NS" >NS-Nova Scotia</option>
				<option value="NU" >NU-Nunavut</option>
				<option value="ON" >ON-Ontario</option>
				<option value="PE" >PE-Prince Edward Island</option>
				<option value="QC" >QC-Quebec</option>
				<option value="SK" >SK-Saskatchewan</option>
				<option value="YT" >YT-Yukon</option>
				<option value="US" >US resident</option>
				<option value="US-AK" >US-AK-Alaska</option>
				<option value="US-AL" >US-AL-Alabama</option>
				<option value="US-AR" >US-AR-Arkansas</option>
				<option value="US-AZ" >US-AZ-Arizona</option>
				<option value="US-CA" >US-CA-California</option>
				<option value="US-CO" >US-CO-Colorado</option>
				<option value="US-CT" >US-CT-Connecticut</option>
				<option value="US-CZ" >US-CZ-Canal Zone</option>
				<option value="US-DC" >US-DC-District Of Columbia</option>
				<option value="US-DE" >US-DE-Delaware</option>
				<option value="US-FL" >US-FL-Florida</option>
				<option value="US-GA" >US-GA-Georgia</option>
				<option value="US-GU" >US-GU-Guam</option>
				<option value="US-HI" >US-HI-Hawaii</option>
				<option value="US-IA" >US-IA-Iowa</option>
				<option value="US-ID" >US-ID-Idaho</option>
				<option value="US-IL" >US-IL-Illinois</option>
				<option value="US-IN" >US-IN-Indiana</option>
				<option value="US-KS" >US-KS-Kansas</option>
				<option value="US-KY" >US-KY-Kentucky</option>
				<option value="US-LA" >US-LA-Louisiana</option>
				<option value="US-MA" >US-MA-Massachusetts</option>
				<option value="US-MD" >US-MD-Maryland</option>
				<option value="US-ME" >US-ME-Maine</option>
				<option value="US-MI" >US-MI-Michigan</option>
				<option value="US-MN" >US-MN-Minnesota</option>
				<option value="US-MO" >US-MO-Missouri</option>
				<option value="US-MS" >US-MS-Mississippi</option>
				<option value="US-MT" >US-MT-Montana</option>
				<option value="US-NC" >US-NC-North Carolina</option>
				<option value="US-ND" >US-ND-North Dakota</option>
				<option value="US-NE" >US-NE-Nebraska</option>
				<option value="US-NH" >US-NH-New Hampshire</option>
				<option value="US-NJ" >US-NJ-New Jersey</option>
				<option value="US-NM" >US-NM-New Mexico</option>
				<option value="US-NU" >US-NU-Nunavut</option>
				<option value="US-NV" >US-NV-Nevada</option>
				<option value="US-NY" >US-NY-New York</option>
				<option value="US-OH" >US-OH-Ohio</option>
				<option value="US-OK" >US-OK-Oklahoma</option>
				<option value="US-OR" >US-OR-Oregon</option>
				<option value="US-PA" >US-PA-Pennsylvania</option>
				<option value="US-PR" >US-PR-Puerto Rico</option>
				<option value="US-RI" >US-RI-Rhode Island</option>
				<option value="US-SC" >US-SC-South Carolina</option>
				<option value="US-SD" >US-SD-South Dakota</option>
				<option value="US-TN" >US-TN-Tennessee</option>
				<option value="US-TX" >US-TX-Texas</option>
				<option value="US-UT" >US-UT-Utah</option>
				<option value="US-VA" >US-VA-Virginia</option>
				<option value="US-VI" >US-VI-Virgin Islands</option>
				<option value="US-VT" >US-VT-Vermont</option>
				<option value="US-WA" >US-WA-Washington</option>
				<option value="US-WI" >US-WI-Wisconsin</option>
				<option value="US-WV" >US-WV-West Virginia</option>
				<option value="US-WY" >US-WY-Wyoming</option>
				<option value="OT">Other</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Postal Code</label>
			<input type="text" class="form-control form-control-details" placeholder="Postal Code" title="Postal Code" ng-model="page.demo.address.postal" ng-change="checkPostal()" ng-blur="isPostalComplete()"/>
		</div>
		<div class="col-md-6">
			<label>Email</label>
			<input type="text" class="form-control form-control-details" placeholder="Email" title="Email" ng-model="page.demo.email"/>
		</div>
		<div class="col-md-6">
			<label style="background-color: {{page.cellPhonePreferredColor}}" title="{{page.cellPhonePreferredMsg}}">
				Cell Phone
				<input type="checkbox" ng-model="page.preferredPhone" ng-change="setPreferredPhone()" ng-true-value="C" ng-disabled="isPhoneVoid(page.cellPhone)"/>
			</label>
			<input type="text" class="form-control form-control-details" placeholder="Cell Phone" title="Cell Phone" ng-model="page.cellPhone" ng-change="checkPhone('C')"/>
		</div>
		<div class="col-md-6">
			<label style="background-color: {{page.homePhonePreferredColor}}" title="{{page.homePhonePreferredMsg}}">
				Home Phone
				<input type="checkbox" ng-model="page.preferredPhone" ng-change="setPreferredPhone()" ng-true-value="H" ng-disabled="isPhoneVoid(page.homePhone)"/>
			</label>
			<span style="white-space:nowrap">
				<input type="text" class="form-control form-control-details" placeholder="Home Phone" title="Home Phone" ng-model="page.homePhone" ng-change="checkPhone('H')" style="width:130px"/>
				<input type="text" class="form-control form-control-details" placeholder="Ext" title="Home Phone Extension" ng-model="page.hPhoneExt.value" ng-change="checkPhone('HX')" style="width:65px"/>
			</span>
		</div>
		<div class="col-md-6">
			<label style="background-color: {{page.workPhonePreferredColor}}" title="{{page.workPhonePreferredMsg}}">
				Work Phone
				<input type="checkbox" ng-model="page.preferredPhone" ng-change="setPreferredPhone()" ng-true-value="W" ng-disabled="isPhoneVoid(page.workPhone)"/>
			</label>
			<span style="white-space:nowrap">
				<input type="text" class="form-control form-control-details" placeholder="Work Phone" title="Work Phone" ng-model="page.workPhone" ng-change="checkPhone('W')" style="width:130px"/>
				<input type="text" class="form-control form-control-details" placeholder="Ext" title="Work Phone Extension" ng-model="page.wPhoneExt.value" ng-change="checkPhone('WX')" style="width:65px"/>
			</span>
		</div>
		<div class="col-md-11">
			<label>Phone Comment</label>
			<input type="text" class="form-control form-control-details" placeholder="Phone Comment" title="Phone Comment" ng-model="page.phoneComment.value" style="width:520px"/>
		</div>
		<div class="col-md-6">
			<label>Newsletter</label>
			<select class="form-control form-control-details" title="Newsletter" ng-model="page.demo.newsletter">
				<option value="No">No</option>
				<option value="Paper">Paper</option>
				<option value="Electronic">Electronic</option>
			</select>
		</div>
	</div>
	
	<div class="form-group row">
		<fieldset>
			<legend>Health Insurance</legend>
		</fieldset>
		<div class="col-md-6">
			<label>
				HIN #
				<span ng-show="page.HCValidation=='valid'" title="HIN Valid" style="font-size:large; color:#009900">&#10004;</span>
				<span ng-show="page.HCValidation=='invalid'" title="HIN Invalid" style="font-size:large; color:red">&#10008;</span>
				<span ng-show="page.HCValidation=='n/a'" title="Health Card Validation not ready" style="font-size:large; color:#ff5500">?</span>
				<button class="btn" title="Validate HIN #" ng-click="validateHC()" ng-hide="page.demo.hin==null || page.demo.hin=='' || page.demo.hcType!='ON'" style="padding: 0px 5px; font-size: small">Validate</button>
			</label>
			<span style="white-space:nowrap">
				<input type="text" class="form-control form-control-details" placeholder="HIN #" title="HIN #" ng-model="page.demo.hin" ng-change="checkHin()" style="width:140px; background-color:{{page.hinColor}}"/>
				<input type="text" class="form-control form-control-details" placeholder="Ver" title="HIN Version" ng-model="page.demo.ver" ng-change="checkHinVer()" style="width:55px; background-color:{{page.verColor}}"/>
			</span>
		</div>
		<div class="col-md-6">
			<label>Health Card Type</label>
			<select class="form-control form-control-details" title="Health Card Type" ng-model="page.demo.hcType" style="background-color:{{page.hcTypeColor}}">
				<option value="" >--</option>
				<option value="AB" >AB-Alberta</option>
				<option value="BC" >BC-British Columbia</option>
				<option value="MB" >MB-Manitoba</option>
				<option value="NB" >NB-New Brunswick</option>
				<option value="NL" >NL-Newfoundland Labrador</option>
				<option value="NT" >NT-Northwest Territory</option>
				<option value="NS" >NS-Nova Scotia</option>
				<option value="NU" >NU-Nunavut</option>
				<option value="ON" >ON-Ontario</option>
				<option value="PE" >PE-Prince Edward Island</option>
				<option value="QC" >QC-Quebec</option>
				<option value="SK" >SK-Saskatchewan</option>
				<option value="YT" >YT-Yukon</option>
				<option value="US" >US resident</option>
				<option value="US-AK" >US-AK-Alaska</option>
				<option value="US-AL" >US-AL-Alabama</option>
				<option value="US-AR" >US-AR-Arkansas</option>
				<option value="US-AZ" >US-AZ-Arizona</option>
				<option value="US-CA" >US-CA-California</option>
				<option value="US-CO" >US-CO-Colorado</option>
				<option value="US-CT" >US-CT-Connecticut</option>
				<option value="US-CZ" >US-CZ-Canal Zone</option>
				<option value="US-DC" >US-DC-District Of Columbia</option>
				<option value="US-DE" >US-DE-Delaware</option>
				<option value="US-FL" >US-FL-Florida</option>
				<option value="US-GA" >US-GA-Georgia</option>
				<option value="US-GU" >US-GU-Guam</option>
				<option value="US-HI" >US-HI-Hawaii</option>
				<option value="US-IA" >US-IA-Iowa</option>
				<option value="US-ID" >US-ID-Idaho</option>
				<option value="US-IL" >US-IL-Illinois</option>
				<option value="US-IN" >US-IN-Indiana</option>
				<option value="US-KS" >US-KS-Kansas</option>
				<option value="US-KY" >US-KY-Kentucky</option>
				<option value="US-LA" >US-LA-Louisiana</option>
				<option value="US-MA" >US-MA-Massachusetts</option>
				<option value="US-MD" >US-MD-Maryland</option>
				<option value="US-ME" >US-ME-Maine</option>
				<option value="US-MI" >US-MI-Michigan</option>
				<option value="US-MN" >US-MN-Minnesota</option>
				<option value="US-MO" >US-MO-Missouri</option>
				<option value="US-MS" >US-MS-Mississippi</option>
				<option value="US-MT" >US-MT-Montana</option>
				<option value="US-NC" >US-NC-North Carolina</option>
				<option value="US-ND" >US-ND-North Dakota</option>
				<option value="US-NE" >US-NE-Nebraska</option>
				<option value="US-NH" >US-NH-New Hampshire</option>
				<option value="US-NJ" >US-NJ-New Jersey</option>
				<option value="US-NM" >US-NM-New Mexico</option>
				<option value="US-NU" >US-NU-Nunavut</option>
				<option value="US-NV" >US-NV-Nevada</option>
				<option value="US-NY" >US-NY-New York</option>
				<option value="US-OH" >US-OH-Ohio</option>
				<option value="US-OK" >US-OK-Oklahoma</option>
				<option value="US-OR" >US-OR-Oregon</option>
				<option value="US-PA" >US-PA-Pennsylvania</option>
				<option value="US-PR" >US-PR-Puerto Rico</option>
				<option value="US-RI" >US-RI-Rhode Island</option>
				<option value="US-SC" >US-SC-South Carolina</option>
				<option value="US-SD" >US-SD-South Dakota</option>
				<option value="US-TN" >US-TN-Tennessee</option>
				<option value="US-TX" >US-TX-Texas</option>
				<option value="US-UT" >US-UT-Utah</option>
				<option value="US-VA" >US-VA-Virginia</option>
				<option value="US-VI" >US-VI-Virgin Islands</option>
				<option value="US-VT" >US-VT-Vermont</option>
				<option value="US-WA" >US-WA-Washington</option>
				<option value="US-WI" >US-WI-Wisconsin</option>
				<option value="US-WV" >US-WV-West Virginia</option>
				<option value="US-WY" >US-WY-Wyoming</option>
				<option value="OT">Other</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Effective Date</label>
			<input id="effDate" ng-model="page.demo.effDate" type="text" class="form-control form-control-details" title="Health Card Effective Date" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.effDatePicker" ng-click="page.effDatePicker = true" placeholder="YYYY-MM-DD" style="background-color:{{page.effDateColor}}">
		</div>
		<div class="col-md-6">
			<label>Renew Date</label>
			<input id="hcRenewDate" ng-model="page.demo.hcRenewDate" type="text" class="form-control form-control-details" title="Health Card Renew Date" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.hcRenewDatePicker" ng-click="page.hcRenewDatePicker = true" placeholder="YYYY-MM-DD" style="background-color:{{page.hcRenewDateColor}}">
		</div>
	</div>
	
	<div class="form-group row">
		<fieldset>
			<legend>Care Team</legend>
		</fieldset>
		<div class="col-md-6">
			<label>MRP</label>
			<select class="form-control form-control-details" title="MRP" ng-model="page.demo.providerNo" ng-options="mrp.providerNo as mrp.name for mrp in page.demo.doctors">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Nurse</label>
			<select class="form-control form-control-details" title="Nurse" ng-model="page.demo.nurse" ng-options="ns.providerNo as ns.name for ns in page.demo.nurses">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Midwife</label>
			<select class="form-control form-control-details" title="Midwife" ng-model="page.demo.midwife" ng-options="mw.providerNo as mw.name for mw in page.demo.midwives">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Resident</label>
			<select class="form-control form-control-details" title="Resident" ng-model="page.demo.resident" ng-options="res.providerNo as res.name for res in page.demo.doctors">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Referral Doctor</label>
			<input type="text" class="form-control form-control-details" placeholder="Referral Doctor" title="Referral Doctor" ng-model="page.referralDoc"/>
		</div>
		<div class="col-md-6">
			<label>Referral Doctor #</label>
			<input type="text" class="form-control form-control-details" placeholder="Referral Doctor #" title="Referral Doctor #" ng-model="page.referralDocNo" style="width:130px"/>
			<button type="button" class="btn btn-sm" ng-click="showReferralDocList()">Search</button>
			<div style="position: absolute; right: 25px; z-index: 1; background-color: white" ng-show="page.showReferralDocList">
				<select class="form-control form-control-details" title="Pick a referral doctor" size="7" ng-model="page.referralDocObj" ng-options="rfd.label for rfd in page.demo.referralDoctors" ng-click="fillReferralDoc()">
					<option value="">--Pick a referral doctor--</option>
				</select>
			</div>
		</div>
		<div class="col-md-6">
			<label>Roster Status</label>
			<select class="form-control form-control-details" title="Roster Status" ng-model="page.demo.rosterStatus" ng-options="rs.value as rs.label for rs in page.demo.rosterStatusList" style="width:150px"/>
			<button type="button" class="btn btn-sm" title="Add new roster status" ng-click="showAddNewRosterStatus()">Add</button>
			<div style="position: absolute; right: 55px; top: 0px; z-index: 1; background-color: #EEEEEE; padding: 5px;" ng-show="page.showAddNewRosterStatus">
				<input type="text" class="form-control" placeholder="New Roster Status" ng-model="page.newRosterStatus"/>
				<button type="button" class="btn" ng-click="addNewRosterStatus()">Add Status</button>
				<button type="button" class="btn" ng-click="showAddNewRosterStatus()">Cancel</button>
			</div>
		</div>
		<div class="col-md-6">
			<label>Date Rostered</label>
			<input id="rosterDate" ng-model="page.demo.rosterDate" type="text" class="form-control form-control-details" title="Roster Date" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.rosterDatePicker" ng-click="page.rosterDatePicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-6" ng-show="isRosterTerminated()">
			<label>Termination Date</label>
			<input id="rosterTerminationDate" ng-model="page.demo.rosterTerminationDate" type="text" class="form-control form-control-details" title="Roster Termination Date" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.rosterTerminationDatePicker" ng-click="page.rosterTerminationDatePicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-11" ng-show="isRosterTerminated()">
			<label>Reason</label>
			<select class="form-control form-control-details" title="Roster Termination Reason" ng-model="page.demo.rosterTerminationReason" style="width:520px">
				<option value="39"  >Assigned member status ended; roster transferred per physician request</option>
				<option value="59"  >Enrolment ended; patient out of geographic area</option>
				<option value="57"  >Enrolment terminated by patient</option>
				<option value="12"  >Health Number error</option>
				<option value="38"  >Long Term Care enrolment ended; patient has left Long Term Care</option>
				<option value="82"  >Ministry has not received enrolment/ Consent form</option>
				<option value="60"  >No current eligibility</option>
				<option value="73"  >No current eligibility</option>
				<option value="74"  >No current eligibility</option>
				<option value="37"  >Original enrolment ended; patient now enrolled as Long Term Care</option>
				<option value="36"  >Original enrolment ended; patient now re-enroled</option>
				<option value="24"  >Patient added to roster in error</option>
				<option value="14"  >Patient identified as deceased on ministry database</option>
				<option value="51"  >Patient no longer meets selection criteria for your roster</option>
				<option value="41"  >Patient no longer meets selection criteria for your roster - assigned to another physician</option>
				<option value="61"  >Patient out of geographic area; address over-ride applied</option>
				<option value="62"  >Patient out of geographic area; address over-ride removed</option>
				<option value="35"  >Patient transferred from roster per physician request</option>
				<option value="42"  >Physician ended enrolment; patient entered Long Term Care facility</option>
				<option value="54"  >Physician ended enrolment; patient left province</option>
				<option value="53"  >Physician ended enrolment; patient moved out of geographic area</option>
				<option value="56"  >Physician ended enrolment; per patient request</option>
				<option value="44"  >Physician ended patient enrolment</option>
				<option value="40"  >Physician reported member as deceased</option>
				<option value="32"  >Pre-member/ Assigned member ended; now enrolled or registered with photo health card</option>
				<option value="30"  >Pre-member/ Assigned member ended; now enrolled or registered with red and white health card</option>
				<option value="33"  >Termination reason cannot be released (due to patient confidentiality)</option>
				<option value="84"  >Termination reason cannot be released (due to patient confidentiality)</option>
				<option value="90"  >Termination reason cannot be released (due to patient confidentiality)</option>
				<option value="91"  >Termination reason cannot be released (due to patient confidentiality)</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Patient Status</label>
			<select class="form-control form-control-details" title="Patient Status" ng-model="page.demo.patientStatus" ng-options="ps.value as ps.label for ps in page.demo.patientStatusList" ng-blur="checkPatientStatus()" style="width:150px"/>
			<button type="button" class="btn btn-sm" title="Add new patient status" ng-click="showAddNewPatientStatus()">Add</button>
			<div style="position: absolute; right: 55px; top: 0px; z-index: 1; background-color: #EEEEEE; padding: 5px;" ng-show="page.showAddNewPatientStatus">
				<input type="text" class="form-control" placeholder="New Patient Status" ng-model="page.newPatientStatus"/>
				<button type="button" class="btn" ng-click="addNewPatientStatus()">Add Status</button>
				<button type="button" class="btn" ng-click="showAddNewPatientStatus()">Cancel</button>
			</div>
		</div>
		<div class="col-md-6">
			<label>Status Date</label>
			<input id="patientStatusDate" ng-model="page.demo.patientStatusDate" type="text" class="form-control form-control-details" title="Patient Status Date" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.patientStatusDatePicker" ng-click="page.patientStatusDatePicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-6">
			<label>Date Joined</label>
			<input id="dateJoined" ng-model="page.demo.dateJoined" type="text" class="form-control form-control-details" title="Date Joined" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.dateJoinedPicker" ng-click="page.dateJoinedPicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-6">
			<label>End Date</label>
			<input id="endDate" ng-model="page.demo.endDate" type="text" class="form-control form-control-details" title="End Date" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.endDatePicker" ng-click="page.endDatePicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-6">
			<label>Chart Number</label>
			<input type="text" class="form-control form-control-details" placeholder="Chart Number" title="Chart Number" ng-model="page.demo.chartNo"/>
		</div>
		<div class="col-md-6">
			<label>Cytology #</label>
			<input type="text" class="form-control form-control-details" placeholder="Cytology #" title="Cytology #" ng-model="page.cytolNum.value"/>
		</div>
	</div>
	
	<div class="form-group row">
		<fieldset>
			<legend>Additional Information</legend>
		</fieldset>
		<div class="col-md-6">
			<label style="width:150px">Archived Paper Chart</label>
			<select class="form-control form-control-details" title="Archived Paper Chart" ng-model="page.paperChartArchived.value" style="width:175px">
				<option value="NO">No</option>
				<option value="YES">Yes</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Archive Date</label>
			<input id="paperChartArchivedDate" ng-model="page.paperChartArchivedDate.value" type="text" class="form-control form-control-details" title="Paper Chart Archive Date" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.paperChartArchivedDatePicker" ng-click="page.paperChartArchivedDatePicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-6">
			<label>Waiting List</label>
			<select class="form-control form-control-details" title="Waiting List" ng-model="page.demo.waitingListID" ng-options="wln.id as wln.name for wln in page.demo.waitingListNames">
				<option value="">--</option>
			</select>
		</div>
		<div class="col-md-6">
			<label>Date of Request</label>
			<input id="onWaitingListSinceDate" ng-model="page.demo.onWaitingListSinceDate" type="text" class="form-control form-control-details" title="Date of Request" datepicker-popup="yyyy-MM-dd" datepicker-append-to-body="true" is-open="page.onWaitingListSinceDatePicker" ng-click="page.onWaitingListSinceDatePicker = true" placeholder="YYYY-MM-DD">
		</div>
		<div class="col-md-11">
			<label>Waiting List Note</label>
			<input type="text" class="form-control form-control-details" placeholder="Waiting List Note" title="Waiting List Note" ng-model="page.demo.waitingListNote" style="width:520px"/>
		</div>
		<div class="col-md-6">
			<label>Privacy Consent</label>
			<input type="text" class="form-control form-control-details" placeholder="Privacy Consent (verbal)" title="Privacy Consent (verbal)" ng-model="page.privacyConsent.value"/>
		</div>
		<div class="col-md-6">
			<label>Informed Consent</label>
			<input type="text" class="form-control form-control-details" placeholder="Informed Consent (verbal)" title="Informed Consent (verbal)" ng-model="page.informedConsent.value"/>
		</div>
		<div class="col-md-6">
			<label style="width:195px">U.S. Resident Consent Form</label>
			<input type="text" class="form-control form-control-details" placeholder="U.S. Resident Consent Form" title="U.S. Resident Consent Form" ng-model="page.usSigned.value" style="width:130px"/>
		</div>
		<div class="col-md-11">
			<label>Security Question</label>
			<select class="form-control form-control-details" title="Select a Security Question" ng-model="page.securityQuestion1.value" style="width:520px">
				<option>What was the name of your high school?</option>
				<option>What is your spouse's maiden name?</option>
				<option>What is the name of the street you grew up on?</option>
				<option>In what city were you born?</option>
				<option>What is the middle name of your oldest child?</option>
				<option>What is your oldest cousin's first name?</option>
				<option>What is your mother's middle name?</option>
				<option>What is your grandmother's first name?</option>
				<option>What year did you graduate from high school?</option>
			</select>
		</div>
		<div class="col-md-11">
			<label>Answer</label>
			<input type="text" class="form-control form-control-details" title="Answer to Security Question" ng-model="page.securityAnswer1.value" style="width:520px"/>
		</div>
	</div>
	</div>
</div>
<br/>

<div class="col-lg-4">
	<div class="clearfix">
	<img class="pull-left" id="photo" title="Click to upload photo" ng-click="launchPhoto()" ng-src="../imageRenderingServlet?source=local_client&clientId={{page.demo.demographicNo}}"/>
		<div class="pull-left" style="margin-left:5px;">
			<address>
	  			<strong>{{page.demo.lastName}}, {{page.demo.firstName}}</strong><br/>
		  		{{page.demo.address.address}}<br/>
		  		{{page.demo.address.city}}, {{page.demo.address.province}} {{page.demo.address.postal}}<br/>
	  			<abbr title="Phone">P:</abbr> {{page.preferredPhoneNumber}}
			</address>
			<a ng-click="macPHRDo('Verification')" ng-show="page.macPHRIdsSet">MyOSCAR{{page.macPHRVerificationLevel}}</a>
		</div>
	</div>
	<br/>
	<div>
		<div id="pd2" ng-click="checkAction($event)" ng-keypress="checkAction($event)">
		<fieldset>
			<legend>Alerts</legend>
			<textarea ng-model="page.demo.alert" class="form-control form-control-details" style="height:55px; width:100%; color:red;"></textarea>
		</fieldset>
		<br/>
		<fieldset>
			<legend>Notes</legend>
			<textarea ng-model="page.notes" class="form-control form-control-details" style="height:55px; width:100%;"></textarea>
		</fieldset>
		</div>
		<br/>
		<fieldset>
			<legend>
				Contacts
				<button type="button" class="btn" ng-click="manageContacts()">Manage</button>
			</legend>
			<div class="form-group" ng-repeat="dc in page.demo.demoContacts">
				<div class="col-md-12" style="font-weight:bold">{{dc.role}}</div>
				<div class="col-md-7" style="white-space:nowrap">{{dc.lastName}}, {{dc.firstName}}</div>
				<div class="col-md-5">{{dc.phone}}</div>
			</div>
		</fieldset>
		<br/>
		<fieldset>
			<legend>
				Professional Contacts
				<button type="button" class="btn" ng-click="manageContacts()">Manage</button>
			</legend>
			<div class="form-group" ng-repeat="dc in page.demo.demoContactPros">
				<div class="col-md-12" style="font-weight:bold">{{dc.role}}</div>
				<div class="col-md-7" style="white-space:nowrap">{{dc.lastName}}, {{dc.firstName}}</div>
				<div class="col-md-5">{{dc.phone}}</div>
			</div>
		</fieldset>
	</div>
</div>
<br/>
</div>