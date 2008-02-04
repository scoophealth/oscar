//=============================================================================
//
//  Functions used by the print and export dialogs
//
//=============================================================================


function check(obj)
{
    return !obj.disabled;
}

function toggleRangeFields(obj)
{
    if (obj.value == "all")
    {
        document.forms['dlgform'].from.disabled = true;
        document.forms['dlgform'].to.disabled = true;
    }
    else
    {
        document.forms['dlgform'].from.disabled = false;
        document.forms['dlgform'].to.disabled = false;
    }

    return check(obj);
}

function isValidNumber(number)
{
    var nonDigit = /\D+/;

    if (nonDigit.test(number) ||
        number == '0' ||
        number == "")
    {
        return false;
    }

    return true;
}

function checkDisableRange()
{
    var dropdown = document.forms['dlgform'].exportformat;
    if (dropdown == null || document.forms['dlgform'].isRange == null)
    	return;
    	
    if (dropdown.value == "CrystalReports" || dropdown.value == "RecordToMSExcel" || dropdown.value == "CharacterSeparatedValues")
    {        
	document.forms['dlgform'].isRange[0].checked = true;
	document.forms['dlgform'].isRange[1].disabled = true;
	document.forms['dlgform'].from.disabled = true;
	document.forms['dlgform'].to.disabled = true;        	
    }
    else
    {
        document.forms['dlgform'].isRange[1].disabled = false;
    }
}

function getExportRecordFormat()
{
    var dropdown = document.forms['dlgform'].exportformat;
    if (dropdown == null || document.forms['dlgform'].isRecRange == null)
    	return;
    	
    var exportRecFormat = dropdown.value;
    
    if (document.forms['dlgform'].isRecRange[0].checked)
    	exportRecFormat = exportRecFormat + "All";
    	
    return exportRecFormat;
}
