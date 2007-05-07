
function validateForm(form,tab,submit) {
	
	
	if(tab  == 'Page 1') {		
		//var reference = form.elements['data.value(1_2_1)'].value;
		
		//if(reference == '') {
		//	alert('Please provide a reference number');
		//	return false;
		//}
	}

	if(tab == 'Page 4') {
		//intake
		var i1 = parseInt(form.elements['data.value(4_3_1)'].value);
		var i2 = parseInt(form.elements['data.value(4_3_2)'].value);
		var i3 = parseInt(form.elements['data.value(4_3_3)'].value);
		
		if(isNaN(i1) || isNaN(i2) || isNaN(i3)) {
			alert('Please Enter numbers values (0-100) for question 1');
			return false;
		}
		
		var itotal = i1+i2+i3;
		if(itotal != 100) {
			alert('Question 1: values do not add up to 100%.\nPlease check your answers');
			return false;
		}
		
		var i4 = parseInt(form.elements['data.value(4_3_5)'].value);
		var i5 = parseInt(form.elements['data.value(4_3_6)'].value);
		var i6 = parseInt(form.elements['data.value(4_3_7)'].value);	
		
		if( isNaN(i4) || isNaN(i5) || isNaN(i6)) {
			alert('Please Enter numbers values (0-100) for question 1');
			return false;
		}
		
		itotal = i4+i5+i6;
		if(itotal != 100) {
			alert('Question 1: values do not add up to 100%.\nPlease check your answers');
			return false;
		}
		
		//referrals
		var r1 = parseInt(form.elements['data.value(4_4_1)'].value);
		var r2 = parseInt(form.elements['data.value(4_4_2)'].value);
		var r3 = parseInt(form.elements['data.value(4_4_3)'].value);
			
		if(isNaN(r1) || isNaN(r2) || isNaN(r3)) {
			alert('Please Enter integer values for question 2');
			return false;
		}
		
		var rtotal = r1+r2+r3;
		if(rtotal != 100) {
			alert('Question 2: values do not add up to 100%.\nPlease check your answers');
			return false;
		}		


		//queuing
		var q1 = parseInt(form.elements['data.value(4_5_1)'].value);
		var q2 = parseInt(form.elements['data.value(4_5_2)'].value);
		var q3 = parseInt(form.elements['data.value(4_5_3)'].value);
		
		if(isNaN(q1) || isNaN(q2) || isNaN(q3)) {
			alert('Please Enter number values (0-100) for question 3');
			return false;
		}
		
		var qtotal = q1+q2+q3;
		if(qtotal != 100) {
			alert('Question 3: values do not add up to 100%.\nPlease check your answers');
			return false;
		}	
		
		var q4 = parseInt(form.elements['data.value(4_5_5)'].value);
		var q5 = parseInt(form.elements['data.value(4_5_6)'].value);
		var q6 = parseInt(form.elements['data.value(4_5_7)'].value);	
		
		if(isNaN(q4) || isNaN(q5) || isNaN(q6)) {
			alert('Please Enter number values (0-100) for question 3');
			return false;
		}
		
		qtotal = q4+q5+q6;
		if(qtotal != 100) {
			alert('Question 3: values do not add up to 100%.\nPlease check your answers');
			return false;
		}				

		//admission
		var a1 = parseInt(form.elements['data.value(4_6_1)'].value);
		var a2 = parseInt(form.elements['data.value(4_6_2)'].value);
		var a3 = parseInt(form.elements['data.value(4_6_3)'].value);
			
		if(isNaN(a1) || isNaN(a2) || isNaN(a3)) {
			alert('Please Enter integer values for question 4');
			return false;
		}
		
		var atotal = a1+a2+a3;
		if(atotal != 100) {
			alert('Question 4: values do not add up to 100%.\nPlease check your answers');
			return false;
		}		


		//orders/ticklers
		var o1 = parseInt(form.elements['data.value(4_7_1)'].value);
		var o2 = parseInt(form.elements['data.value(4_7_2)'].value);
		var o3 = parseInt(form.elements['data.value(4_7_3)'].value);
		
		var o4 = parseInt(form.elements['data.value(4_7_5)'].value);
		var o5 = parseInt(form.elements['data.value(4_7_6)'].value);
		var o6 = parseInt(form.elements['data.value(4_7_7)'].value);
			
		var o7 = parseInt(form.elements['data.value(4_7_9)'].value);	
		var o8 = parseInt(form.elements['data.value(4_7_10)'].value);	
		var o9 = parseInt(form.elements['data.value(4_7_11)'].value);	
		
		if(isNaN(o1) || isNaN(o2) || isNaN(o3) || isNaN(o4) || isNaN(o5) || isNaN(o6) || isNaN(o7) || isNaN(o8) || isNaN(o9)) {
			alert('Please Enter number values (0-100) values for question 5');
			return false;
		}
		
		var ototal = o1+o2+o3;
		if(ototal != 100) {
			alert('Question 5: values do not add up to 100%.\nPlease check your answers');
			return false;
		}		
		
		ototal = o4+o5+o6;
		if(ototal != 100) {
			alert('Question 5: values do not add up to 100%.\nPlease check your answers');
			return false;
		}
		
		ototal = o7+o8+o9;
		if(ototal != 100) {
			alert('Question 5: values do not add up to 100%.\nPlease check your answers');
			return false;
		}
		
		
	}


	if(tab == 'Page 5') {
		//case notes
		var o1 = parseInt(form.elements['data.value(5_1_1)'].value);
		var o2 = parseInt(form.elements['data.value(5_1_2)'].value);
		var o3 = parseInt(form.elements['data.value(5_1_3)'].value);
		
		var o4 = parseInt(form.elements['data.value(5_1_5)'].value);
		var o5 = parseInt(form.elements['data.value(5_1_6)'].value);
		var o6 = parseInt(form.elements['data.value(5_1_7)'].value);	
		
		var o7 = parseInt(form.elements['data.value(5_1_9)'].value);	
		var o8 = parseInt(form.elements['data.value(5_1_10)'].value);	
		var o9 = parseInt(form.elements['data.value(5_1_11)'].value);	
		
		if(isNaN(o1) || isNaN(o2) || isNaN(o3) || isNaN(o4) || isNaN(o5) || isNaN(o6) || isNaN(o7) || isNaN(o8) || isNaN(o9)) {
			alert('Please Enter number values (0-100) for question 6');
			return false;
		}
		
		var ototal = o1+o2+o3;
		if(ototal != 100) {
			alert('Question 6: values do not add up to 100%.\nPlease check your answers');
			return false;
		}				
		
		ototal = o4+o5+o6;
		if(ototal != 100) {
			alert('Question 6: values do not add up to 100%.\nPlease check your answers');
			return false;
		}
		
		ototal = o7+o8+o9;
		if(ototal != 100) {
			alert('Question 6: values do not add up to 100%.\nPlease check your answers');
			return false;
		}
	
	
		//prescriptions
		var i1 = parseInt(form.elements['data.value(5_2_1)'].value);
		var i2 = parseInt(form.elements['data.value(5_2_2)'].value);
		var i3 = parseInt(form.elements['data.value(5_2_3)'].value);
		
		var i4 = parseInt(form.elements['data.value(5_2_5)'].value);
		var i5 = parseInt(form.elements['data.value(5_2_6)'].value);
		var i6 = parseInt(form.elements['data.value(5_2_7)'].value);	
		
		if(isNaN(i1) || isNaN(i2) || isNaN(i3) || isNaN(i4) || isNaN(i5) || isNaN(i6)) {
			alert('Please Enter number values (0-100) for question 7');
			return false;
		}
		
		var itotal = i1+i2+i3;
		if(itotal != 100) {
			alert('Question 7: values do not add up to 100%.\nPlease check your answers');
			return false;
		}		
		
		itotal = i4+i5+i6;
		if(itotal != 100) {
			alert('Question 7: values do not add up to 100%.\nPlease check your answers');
			return false;
		}	
	
		//program information	
		var r1 = parseInt(form.elements['data.value(5_3_1)'].value);
		var r2 = parseInt(form.elements['data.value(5_3_2)'].value);
		var r3 = parseInt(form.elements['data.value(5_3_3)'].value);
			
		if(isNaN(r1) || isNaN(r2) || isNaN(r3)) {
			alert('Please Enter integer values for question 8');
			return false;
		}
		
		var rtotal = r1+r2+r3;
		if(rtotal != 100) {
			alert('Question 8: values do not add up to 100%.\nPlease check your answers');
			return false;
		}			
		
		//miscellaneous
		var q1 = parseInt(form.elements['data.value(5_4_1)'].value);
		var q2 = parseInt(form.elements['data.value(5_4_2)'].value);
		var q3 = parseInt(form.elements['data.value(5_4_3)'].value);
		
		var q4 = parseInt(form.elements['data.value(5_4_5)'].value);
		var q5 = parseInt(form.elements['data.value(5_4_6)'].value);
		var q6 = parseInt(form.elements['data.value(5_4_7)'].value);	
		
		if(isNaN(q1) || isNaN(q2) || isNaN(q3) || isNaN(q4) || isNaN(q5) || isNaN(q6)) {
			alert('Please Enter number values (0-100) for question 9');
			return false;
		}
		
		var qtotal = q1+q2+q3;
		if(qtotal != 100) {
			alert('Question 9: values do not add up to 100%.\nPlease check your answers');
			return false;
		}
							
		qtotal = q4+q5+q6;
		if(qtotal != 100) {
			alert('Question 9: values do not add up to 100%.\nPlease check your answers');
			return false;
		}	
	}

	if(tab != 'Page 5' && submit == true) {
		alert('Please save form from Page 5. All sections should be completed.');
		return false;
	}
	
	return true;
}