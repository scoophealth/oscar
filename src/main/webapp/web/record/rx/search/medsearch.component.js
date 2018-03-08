const MedsearchComponent = {
  bindings: {
  	medSelected: '&'

  },
  templateUrl: 'record/rx/search/medsearch.template.jsp',
  controller: ['$stateParams','$state','$log','$timeout','summaryService','rxService',function($stateParams,$state,$log,$timeout,summaryService,rxService) {
  	rxSearchComp = this;

  	rxSearchComp.$onInit = function(){
  		console.log("herer??  rxlookup/search");
  		
  		rxSearchComp.favouriteMeds = [];
  		
 		console.log("elementfound"+$('#medQuickSearch'));
  		$('#medQuickSearch').typeahead({
  			name: 'drugSearch',
  			valueKey:'name',
  			limit: 11,
  			prefetch	: {
  				    ttl: 1,
	  				url: '../ws/rs/rx/favorites',
	  	  			filter: function (parsedResponse) {
			        		console.log("fav filter",parsedResponse);
			            retval = [];
			            if(parsedResponse.drugs instanceof Array) {
			            		for (var i = 0;  i < parsedResponse.drugs.length;  i++) {
			            			var tmp = parsedResponse.drugs[i];
			            			tmp.blah = "";
			            			tmp.name = tmp.favoriteName;
			            			tmp.styleClass= 'favHeader';
			            			console.log()
			            			retval.push(tmp);
			                 }
			            } else {
			            	retval.push(parsedResponse.drugs);
			            }
			            
			            //console.log("total:"+retval.length);
			            //var scope = angular.element($("#medQuickSearch")).scope();
			            //setQuickSearchTerm("");
			            
			            //if(parsedResponse.total > 10) {
			            	//retval.push({name:"more results",hin:parsedResponse.total+" total","demographicNo":-1,"more":true});
			            	//setQuickSearchTerm(parsedResponse.query);
			            //}
			            console.log("retval",retval);
			            rxSearchComp.favouriteMeds = retval;
			            return retval;
			        }
	  		},
  			remote: {
  		        //url: '../ws/rs/demographics/quickSearch?query=%QUERY',
  				url: '../ws/rs/rxlookup/search?string=%QUERY',
  		        cache:false,
  		        //I needed to override this to handle the differences in the JSON when it's a single result as opposed to multiple.
  		        filter: function (parsedResponse) {
  		        		console.log("filter",parsedResponse);
  		            retval = [];
  		            if(parsedResponse.drugs instanceof Array) {
  		            		for (var i = 0;  i < parsedResponse.drugs.length;  i++) {
	  		            		var tmp = parsedResponse.drugs[i];
	  		            		//if(tmp.hin != null && tmp.hin == '') {
	  		            		//	tmp.hin = null;
	  		            		//}
	  		            		//if(tmp.formattedDOB != null && tmp.formattedDOB == '') {
	  		            		//	tmp.formattedDOB = null;
	  		            		//}
	  		            		
	  		            		//tmp.name = tmp.lastName + ", " + tmp.firstName;
	  		            		tmp.blah = "";
	  		            		tmp.styleClass= 'searchedHeader';
	  		            		if(!tmp.active){
	  		            			tmp.styleClass='inactiveHeader';
	  		            		}
	  		            		retval.push(tmp);
  		                 }
  		            } else {
  		            	retval.push(parsedResponse.drugs);
  		            }
  		            
  		            console.log("total:"+parsedResponse.total);
  		            var scope = angular.element($("#medQuickSearch")).scope();
  		            setQuickSearchTerm("");
  		            
  		            if(parsedResponse.total > 10) {
  		            	retval.push({name:"more results",hin:parsedResponse.total+" total","demographicNo":-1,"more":true});
  		            	setQuickSearchTerm(parsedResponse.query);
  		            }
  		            
  		            return retval;
  		        }
  		    },
  		    
  			template: [
  			        "<p class='{{styleClass}}'>{{#favoriteName}}<span class='glyphicon glyphicon-star'></span>{{/favoriteName}} {{name}}</p>",
  			        '{{#favoriteName}}<p class="demo-quick-hin">&nbsp;<em>{{brandName}}</em></p>{{/favoriteName}}',
  			       	'{{#dob}}<p class="demo-quick-dob">&nbsp;{{formattedDOB}}</p>{{/dob}}'
  			 ].join(''),
  			       	engine: Hogan
  			}
  				
  			).on('typeahead:selected', function (obj, datum) {
  				//$('input#medQuickSearch').on('blur',function(event){$("#medQuickSearch").val("");});

  				//var scope = angular.element($("#medQuickSearch")).scope();
  						
  				// jQuery#typeahead('setQuery', query)
  				console.log("whats in obj ",obj);
  				obj.delegateTarget.value = '';	
  				
  				
  				
  				
  				if(datum.more != null && datum.more == true) {
  					scope.switchToAdvancedView();
  				} else {
  					rxSearchComp.medSelected({med:datum});
  					//scope.loadRecord(datum.demographicNo);
  				}
  				
  				
  				
  			});
  		
  		
 	}
 	
 	
  	setQuickSearchTerm = function(term) {
  		console.log("term ",term);
  		rxSearchComp.quickSearchTerm = term;
  		
	}

 	
	
 	} 
  ]
};