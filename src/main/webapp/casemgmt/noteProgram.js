/*
 * Copyright (c) 2008-2012 Indivica Inc.
 *
 * This software is made available under the terms of the
 * GNU General Public License, Version 2, 1991 (GPLv2).
 * License details are available via "indivica.ca/gplv2"
 * and "gnu.org/licenses/gpl-2.0.html".
 */

var _noteProgramClick = function(e) {
	e.stopPropagation();

	if (jQuery(this).parent().children().length == 2)
		jQuery("#_program_popupOpen").remove();
	else {
		jQuery("#_program_popupOpen").remove();

		var parent = jQuery(this).parent();
		var roleName = jQuery(parent).attr("roleName");
		var programName = jQuery(parent).attr("programName");


		var popup = jQuery("#_program_popup").clone();
		jQuery(popup).css("display", "block");
		jQuery(popup).attr("id", "_program_popupOpen");


		if (jQuery(popup).find(".selectProgram option[programName='" + programName + "']").length == 0) {
			// Using "-1" as a value to denote that this note is assigned to a discharged program - this is fine, but we won't let
			// users assign a new note to this program (and if this note is assigned to a different program, it's impossible to assign it
			// back to the discharged program).
			jQuery(popup).find(".selectProgram").append(
					jQuery("<option></option>")
					.attr("value", "-1")
					.attr("programName", programName)
					.attr("selected", "selected")
					.text(programName + " (discharged)")
			);

			jQuery(popup).find(".role").empty();
			jQuery(popup).find(".role").append(
					jQuery("<option></option>")
					.attr("value", "-1")
					.attr("roleName", roleName)
					.attr("selected", "selected")
					.text(roleName)
			);
		}

		jQuery(popup).find(".selectProgram option[programName='" + programName + "']").attr("selected", "selected");
		jQuery(popup).find(".role option[roleName='" + roleName + "']").attr("selected", "selected");

		jQuery(popup).find(".saveBtn").click(_saveBtnClick);
		jQuery(popup).find(".closeBtn").click(_closeBtnClick);
		jQuery(popup).find(".scopeBtn").click(_scopeBtnClick);

		jQuery(parent).prepend(popup);


		// While the dialog box is open, users can change the program; this code makes sure that the role list is
		// always updated to the correct list of roles available for that program.  In the case of discharged programs,
		// the role is fixed and can't be changed.  Needed to use a closure to keep the value available to this asynchronous
		// function call.
		(function (rName) {
			jQuery(popup).find(".selectProgram").change(function() {
				var roleList = jQuery(this).parent().find(".role");
				jQuery(roleList).empty();
				if (jQuery(this).val() == "-1") {
					jQuery(roleList).append(
							jQuery("<option></option>")
							.attr("value", "-1")
							.attr("roleName", rName)
							.text(rName)
					);
				} else {
					for (var r = 0; r < _programRoleMap[jQuery(this).val()].length; r++) {
						jQuery(roleList).append(
								jQuery("<option></option>")
									.attr("value", _programRoleMap[jQuery(this).val()][r].id)
									.attr("roleName", _programRoleMap[jQuery(this).val()][r].name)
									.text(_programRoleMap[jQuery(this).val()][r].name)
						);
					}
				}
			});
		})(roleName);
	}
};

var _noVisibleProgramsError = function() {
	alert("This patient is not currently admitted to any program you have access to.  Click \"Program Admissions\" on the top bar to adjust patient admissions, re-open the chart, and try again.");
};

var _missingRoleProgramIdError = function() {
	alert("Note is missing program number and/or role id.");
};

var _setupNewNote = function() {
	jQuery.ajax({
		url: ctx + "/casemgmt/NotePermissions.do",
		data: "method=getDefaultProgramAndRole&demoNo=" + demographicNo,
		dataType: "json",
		type: "POST",
		success: function(data) {
			if (!data.success) {
				var notePermissionBox = jQuery("<div></div>").attr({
					class: "_program",
					noteId: savedNoteId,
					programName: "-2",
					roleName: "-2"
				}).append(jQuery("<span></span>").attr({
					class: "program"
				}).text("NO CURRENT ADMISSION FOUND")
				.click(_noVisibleProgramsError));

				jQuery(notePermissionBox).insertBefore("#sig" + savedNoteId);

				// Using "-2" as a value to denote that, for the new note, no programs were found - don't allow the user to save this note.
				// It's ok to save a note already assigned to a discharged program, though - so for that, we'll use -1 instead.
				jQuery("form[name='caseManagementEntryForm'] input[name='_note_program_no']").val("-2");
				jQuery("form[name='caseManagementEntryForm'] input[name='_note_role_id']").val("-2");

			} else {
				var notePermissionBox = jQuery("<div></div>").attr({
											class: "_program",
											noteId: savedNoteId,
											programName: data.defaultProgramName,
											roleName: data.defaultRoleName
										}).append(jQuery("<span></span>").attr({
											class: "program"
										}).text(data.defaultProgramName + " (" + data.defaultRoleName + ")")
										.click(_noteProgramClick));

				jQuery(notePermissionBox).insertBefore("#sig" + savedNoteId);

				jQuery("form[name='caseManagementEntryForm'] input[name='_note_program_no']").val(data.defaultProgram);
				jQuery("form[name='caseManagementEntryForm'] input[name='_note_role_id']").val(data.defaultRole);
			}
		}
	});
};

var _saveBtnClick = function(e) {
	var box = jQuery(this).parent().parent();
	var noteId = jQuery(box).parent().parent().attr("noteId");
	var programNo = jQuery(box).find(".selectProgram").val();
	var programName = jQuery(box).find(".selectProgram option[value=" + programNo + "]").text();
	var roleId = jQuery(box).find(".role").val();
	var roleName = jQuery(box).find(".role option[value=" + roleId + "]").text();
	jQuery(this).parent().find("select").attr("disabled", "disabled");

	jQuery(this).unbind("click");

	var btn = jQuery(this);

	if (noteId == "0") {
		var programBar = jQuery(btn).parent().parent().parent().parent();
		jQuery(programBar).find(".program").text(programName + " (" + roleName + ")");
		jQuery(programBar).attr({
			roleName: roleName,
			programName: programName
		});

		jQuery("form[name='caseManagementEntryForm'] input[name='_note_program_no']").val(programNo);
		jQuery("form[name='caseManagementEntryForm'] input[name='_note_role_id']").val(roleId);

		jQuery(btn).parent().parent().parent().remove();
	} else if (programNo == "-1" || roleId == "-1") {
		jQuery(btn).parent().parent().parent().remove();
	} else {
		jQuery.ajax({
			url: ctx + "/casemgmt/NotePermissions.do",
			data: "method=setNotePermissions&noteId=" + noteId + "&program=" + programNo + "&role=" + roleId,
			dataType: "json",
			type: "POST",
			success: function(data) {
				if (data.error == "PERMISSION_DENIED") {
					jQuery(this).parent().parent().find(".errorMessage").text("PERMISSION DENIED");
				} else if (data.error == "MAKES_INVISIBLE") {
					jQuery(this).parent().parent().find(".errorMessage").style("font-size", "11px").text("Invalid action: Makes note invisible");
				} else if (data.success == "SAVED") {
					var programBar = jQuery(btn).parent().parent().parent().parent();
					jQuery(programBar).find(".program").text(data.newProgramName + " (" + data.newRoleName + ")");
					jQuery(programBar).attr({
						roleName: data.newRoleName,
						programName: data.newProgramName,
						noteId: data.newNoteId
					});


					// Make sure we're doing the correct thing for notes that are currently being worked on...
					// The code below is really only for notes that aren't being edited.
					var noteObj = jQuery(programBar).parent().parent();
					jQuery(noteObj).find("#signed" + noteId).attr("id", "signed" + data.newNoteId);
					jQuery(noteObj).find("#full" + noteId).attr("id", "full" + data.newNoteId);
					jQuery(noteObj).find("#bgColour" + noteId).attr("id", "bgColour" + data.newNoteId);
					jQuery(noteObj).find("#editWarn" + noteId).attr("id", "editWarn" + data.newNoteId);
					jQuery(noteObj).find("#quitImg" + noteId).attr("id", "quitImg" + data.newNoteId);
					jQuery(noteObj).find("#print" + noteId).attr("id", "print" + data.newNoteId).attr("onclick", "togglePrint(" + data.newNoteId + ", event)");
					jQuery(noteObj).find("#edit" + noteId).attr("id", "edit" + data.newNoteId);
					jQuery(noteObj).find("#txt" + noteId).attr("id", "txt" + data.newNoteId).html(data.noteContent.replace(/\n/g, "<br />"));
					jQuery(noteObj).find("#sig" + noteId).attr("id", "sig" + data.newNoteId);
					jQuery(noteObj).find("#summary" + noteId).attr("id", "summary" + data.newNoteId);
					jQuery(noteObj).find("#observation" + noteId).attr("id", "observation" + data.newNoteId);
					jQuery(noteObj).find("#observation" + data.NewNoteId + " a").text(parseInt(jQuery(noteObj).find("#observation" + data.NewNoteId + " a").text()) + 1);
					jQuery(noteObj).find("#obs" + noteId).attr("id", "obs" + data.newNoteId);
					jQuery(noteObj).find("#encType" + noteId).attr("id", "encType" + data.newNoteId);


					jQuery(btn).parent().parent().parent().remove();
				} else if (data.success == "NO_CHANGE") {
					jQuery(btn).parent().parent().parent().remove();
				}
			}
		});
	}
};

var _closeBtnClick = function(e) {
	jQuery(this).parent().parent().parent().remove();
};

var _scopeBtnClick = function(e) {
	var noteId = jQuery(this).parent().parent().parent().parent().attr("noteId");
	var programNo = jQuery(this).parent().parent().find(".selectProgram").val();
	var roleId = jQuery(this).parent().parent().find(".role").val();

	var screen = jQuery("#_program_scope").clone();
	jQuery(screen).attr("id", "_program_scopeOpen");

	jQuery(screen).removeAttr("style");
	jQuery(screen).find(".uiBigBarBtn").click(_closeScreenBtn);

	jQuery("body").append(screen);

	jQuery.ajax({
		url: ctx + "/casemgmt/NotePermissions.do",
		data: "method=getNoteScope&noteId=" + noteId + "&programNo=" + programNo + "&roleId=" + roleId,
		dataType: "json",
		type: "POST",
		success: function(data) {

			jQuery(screen).find(".programName").text(data.programName);
			jQuery(screen).find(".roleName").text(data.roleName);

			jQuery(screen).find(".loading").hide();

			for (var i = 0; i < data.permissionList.length; i++) {
				var explanation = "";
				if (data.permissionList[i].accessType == "ROLE")
					explanation = "Granted by role (in program)";
				else if (data.permissionList[i].accessType == "ROLE_GLOBAL")
					explanation = "Granted by role (global)";
				else if (data.permissionList[i].accessType == "ALL_ROLES")
					explanation = "Permission granted to all roles";
				else if (data.permissionList[i].accessType == "NO_ACCESS")
					explanation = "No Access";

				var row = jQuery("<tr></tr>").attr("class", data.permissionList[i].accessType)
						.append(
							jQuery("<th></th>")
							.text(data.permissionList[i].providerLastName + ", " + data.permissionList[i].providerFirstName)
						).append(
							jQuery("<td></td>")
							.text(data.permissionList[i].roleName)
						).append(
							jQuery("<td></td>")
							.text(explanation)
						);

				jQuery(screen).find(".permissions").append(row);

			}


		}
	});
};

var _closeScreenBtn = function(e) {
	jQuery("#_program_scopeOpen").remove();
}

var _programRoleMap = {};

var _setupProgramList = function() {
	jQuery.ajax({
		url: ctx + "/casemgmt/NotePermissions.do",
		data: "method=visibleProgramsAndRoles&demoNo=" + demographicNo,
		dataType: "json",
		type: "POST",
		success: function(data) {
			var programList = jQuery("#_program_popup .selectProgram");
			jQuery(programList).empty();
			for (var p = 0; p < data.programs.length; p++) {
				_programRoleMap[data.programs[p].programNo] = data.programs[p].roleAccess;
				//if (data.programs[p].programName.endsWith("(discharged)"))
				//	data.programs[p].programName = data.programs[p].programName.substring(0, data.programs[p].programName.indexOf("(discharged)") - 1)
				jQuery(programList).append(
					jQuery("<option></option>")
						.attr("value", data.programs[p].programNo)
						.attr("programName", data.programs[p].programName)
						.text(data.programs[p].programName)
				);
			}

			jQuery("#_program_popup .role").empty();
			if (data.programs.length > 0) {
				for (var r = 0; r < _programRoleMap[data.programs[0].programNo].length; r++) {
					jQuery("#_program_popup .role").append(
							jQuery("<option></option>")
								.attr("value", _programRoleMap[data.programs[0].programNo][r].id)
								.attr("roleName", _programRoleMap[data.programs[0].programNo][r].name)
								.text(_programRoleMap[data.programs[0].programNo][r].name)
					);
				}
			}
		}
	});
};

var _setCurrentProgramAndRoleIdForNote = function(noteId) {
	var programName = jQuery("#n" + noteId).find("._program").attr("programname");
	var roleName = jQuery("#n" + noteId).find("._program").attr("rolename");

	var programNo = -1;
	var roleId = -1;

	var programOption = jQuery("#_program_popup .selectProgram option[programName=" + programName + "]");
	if (jQuery(programOption).length > 0) {
		programNo = jQuery("#_program_popup .selectProgram option[programName=" + programName + "]").val();
		for (var r = 0; r < _programRoleMap[programNo].length; r++) {
			if (_programRoleMap[programNo][r].name == roleName)
				roleId = _programRoleMap[programNo][r].id;
		}
	}

	jQuery("input[name=_note_program_no]").val(programNo);
	jQuery("input[name=_note_role_id]").val(roleId);

	if (typeof console != "undefined")
		console.log("Setting program_no=" + programNo + ", role_id=" + roleId);
};