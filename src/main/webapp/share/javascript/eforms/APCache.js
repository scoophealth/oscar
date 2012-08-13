/*
 APCache.js - Copyright 2011 © Indivica 
 Author: Adam Balanga
 
 Description
 ===========
 
 APCache provides an interface for making calls against a web page in 
 OSCAR's eforms which will return the value of queries defined in 
 OSCAR/web/WEB-INF/classes/src/oscar/richeform/apconfig.xml.
 
 Requirements
 ============
 
 APCache requires that you include jQuery on the page. 

 Therefore, it is essential that you include the following line in your richeform. 
 
 <script language="javascript" type="text/javascript" src="${oscar_image_path}jquery-1.4.2.js"></script>

 
 ------------------------------------------------------------------------------
 
 createCache(options)
 ====================
 
 Creates a cache to use for looking up information from the database.  
  
  options: 
    defaultCacheResponseHandler - Set the default function called when a cache
                                  lookup has yielded a response (receives one 
                                  parameter which is the mapping or cache key
                                  looked up).
    cacheResponseErrorHandler   - Set the error handler for an AJAX call 
                                  (receives parameters xhr & error).
  returns - a Cache object.
  
 e.g., The following will create a cache which by default writes any retrieved 
 value to the page and opens an alert popup box with the HTTP error for an AJAX
 call if an error occurs. 
 
 var myCache = createCache({
     defaultCacheResponseHandler: function (param) { 
         document.write(myCache.get(param)); 
     },
     cacheResponseErrorHandler: function(xhr, error) { alert(error); }
 });
 
 ------------------------------------------------------------------------------
 
 Cache Object
 ============
 
 put(key, value) - Insert a key, value pair into the cache 
                   ( e.g., put("age", "23"); ) 
 
 get(key)        - Retrieves the value for a key in the cache 
                   ( e.g., get("age"); )
 
 contains(key)   - Checks if a value has been defined in the cache 
                   (i.e., if the item has been looked for before.)
 
 isEmpty(key)    - Checks if the value for key is defined and not the empty string.
 
 addMapping(mappingOptions) - Adds a {CacheMapping} to this cache for a particular term. 
 
 mappingOptions:
   name                 - the name for this mapping
   values               - the values of the associated fields
   storeInCacheHandler  - function called after all the fields associated with this mapping have been loaded
   cacheResponseHandler - function called after all cache queries have been processed

 e.g., The following will create a mapping for "patientInfo" which will include patient name and age.
        - values is set to the fields required from the AP Config.
        - storeInCacheHandler is called after the values have been returned and allows us to update the cache
          with the information retrieved.
       - cacheResponseHandler is called after all values are retrieved from the cache IF the lookup which generated
         this cache response is equal to the name of this mapping.
          
 myCache.addMapping({
     name: "patientInfo", 
     values: ["patient_name", "age"], 
     storeInCacheHandler: function () { 
          myCache.put(this.name, myCache.get("patient_name") + " is " myCache.get("age") + " years old");
      },
      cacheResponseHandler: function () { alert(myCache.get("patientInfo");) } 
 });
  

 getMapping(mappingKey) - Return the mapping associated with mappingKey or null if no mapping exists. 
 
 lookup(key) - Performs a lookup in the cache.
    key: if there is a mapping for key then the associated values are retrieved otherwise the value for key is retrieved.    
    
    e.g., retrieve the key "age" defined in the apconfig.xml:
    
    myCache.lookup("age");
    
    e.g., retrieve the mapping we defined previously defined in the apconfig.xml:
    
    myCache.lookup("patientInfo");

Below we will modify some of the examples above to create a fully working eform which uses the APCache.

<!-- START EXAMPLE  -->
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=UTF-8">
        
        <title>AP Cache Example</title>
        
        <!-- APCache requires jQuery. !-->
        <script language="javascript" type="text/javascript" src="${oscar_image_path}jquery-1.4.2.js"></script>
        
        <!-- APCache requires APCache. !-->
        <script language="javascript" type="text/javascript" src="${oscar_image_path}APCache.js"></script>        
        
        <script langauge="javascript" type="text/javascript">
        
        // Create a APCache to use.
        var myCache = createCache({
            // Here we use a showValue function that we will define below to handle the data returned.
            defaultCacheResponseHandler: showValue,
             cacheResponseErrorHandler: function(xhr, error) { alert(error); }
        });
         
        // showValue simply places the value for the key provided into the span with id valueField.
        function showValue(key) {
            var value = myCache.get(key);
            jQuery("#valueField").html(value); 
        }
         
        // We will recreate the mapping we prepared above. 
        myCache.addMapping({
            name: "patientInfo", 
            values: ["patient_name", "age"], 
            storeInCacheHandler: function () { 
                myCache.put(this.name, myCache.get("patient_name") + " is " + myCache.get("age") + " years old");
            },
            cacheResponseHandler: function () { alert(myCache.get("patientInfo")); } 
        });
        
        //  We will also create a copy of this mapping which will print to the page, since omitting the 
        // cacheResponseHandler will cause this mapping  to fall back on the default we set for this cache.
        myCache.addMapping({
            name: "patientInfo2", 
            values: ["patient_name", "age"], 
            storeInCacheHandler: function () { 
                myCache.put(this.name, myCache.get("patient_name") + " is " + myCache.get("age") + " years old");
            }
        });
        </script>
    </head>
    
    <body>
    	<!-- Here we have the field we will use to display values retrieved from the database. -->
        <div>The value is: <span id="valueField"></span></div>
        
        <!-- Create buttons to test the mappings we defined above as well as a plain field retrieval. !-->
        <button onclick="myCache.lookup('age')">Patient Age</button>
        <button onclick="myCache.lookup('patientInfo')">Patient Info</button>
        <button onclick="myCache.lookup('patientInfo2')">Patient Info2</button>        
    </body>
</html>
<!-- END EXAMPLE -->                 
 */

function createCache(options) {    
    function Cache(options) {
    	this.flushCache = options.flushCache == null ? true : options.flushCache;
        this.values = {};
        this.mappings = [];    
        this.defaultCacheResponseHandler = options.defaultCacheResponseHandler == null ? function () {} : options.defaultCacheResponseHandler;    
        this.cacheResponseErrorHandler = options.defaultCacheResponseErrorHandler == null
            ? function(xhr, error) { alert("Please contact an administrator, an error has occurred."); } 
                : options.defaultCacheResponseErrorHandler; 
        this.put = function(key, value) {
            return this.values[key] = value.replace(/\r?\n/g,"<br>");
        };
        this.get = function(key) {
            return this.values[key];
        };
        this.contains = function(key) {
            return !(this.values[key] === undefined);
        };
        this.isEmpty = function (key) {
            return this.values[key] == null || this.values[key] == ""; 
        };
        this.addMapping = function (options) {
            if (options.cacheResponseHandler == null) { 
                options.cacheResponseHandler = this.defaultCacheResponseHandler; 
            }
            this.mappings.push(new CacheMapping(options));
        };
        this.getMapping = function (key) {
            var mappingIndex;
            for (mappingIndex = 0; mappingIndex < this.mappings.length; mappingIndex++) { 
                if (this.mappings[mappingIndex].name == key) { return this.mappings[mappingIndex]; } 
            }
            return null;
        };
        this.lookup = function(key) {
            
        	if (this.flushCache) { 
            	this.values = {};
            }
        	if (_cache.contains(key)) { return _cache.get(key); }
        	
            // Compiling keys into name value pairs.
            var query = "oscarAPCacheLookupType=" + key;
            var map;
    
            // Include associated fields if a mapping is found.
            if ((map = this.getMapping(key)) != null) {
                var y = 0;
                for (y = 0; y < map.values.length; y++) {
                    query += "&key=" + map.values[y];
                }                
                // If mapping (A) references mapping (B) then mapping 
                // (B) must be passed along as a key so that the onStoreInCache event for mapping (B)
                // will be executed when the cache returns values.
                if (map.name != key) { 
                    query +="&key=" + map.name;
                }
            }
            // Otherwise, use just this key. 
            else {
                query += "&key=" + key;
            }
            
            if (window.location.search.substr(1).indexOf("demographic_no") < 0 && demographicNo !== undefined) {
            	query += "&demographic_no=" + demographicNo;
            }            
            
            jQuery.ajax({
                url : "efmformapconfig_lookup.jsp",
                data : query + "&" + window.location.search.substr(1),
                success : function(response) {
                    response = jQuery(response);
                    var y;
                    var map;
                    var _lookupType = "";
                    // Adding values retrieved to the cache.
                    for (y = 0; y < response.length; y++) {
                        item = jQuery(response[y]);
                        var _key = item.attr("name");
                        var _val = item.val();
                        
                        if (_key == "oscarAPCacheLookupType") {
                            _lookupType = _val;
                            continue;
                        }
                        // Checking if a key has a mapping.
                        map = _cache.getMapping(_key);
                        if (map != null) {
                            map.onStoreInCache(_key, _val);
                        } 
                        else {
                            _cache.put(_key, _val);
                        }
    
                    }
                    // Performing store in cache for mapping if lookup type matches a mapping 
                    if (_lookupType != null && _lookupType != "") {
                        map = _cache.getMapping(_lookupType);
                        if (map != null) {
                            map.onStoreInCache();
                        }
                    }
                    // Look for mapping and pass off to handler.
                    map = _cache.getMapping(_lookupType);
                    if (map != null) {
                        map.onCacheResponse(_lookupType);
                    }
                    // Otherwise print the cache entry associated with this lookup type.
                    else {
                        _cache.defaultCacheResponseHandler(_lookupType);
                    }
                    
                },
                error: this.cacheResponseErrorHandler
            });
            return null;
        };
    };

    var _cache = new Cache(options);
    return _cache;
}

/**
 * Associates multiple fields with a single key and allows behaviour  
 * for storing in cache and display on the page to be customized.
 *
 * @param name the name for this mapping
 * @param values the values of the associated fields
 * @param storeInCacheHandler function called after all the fields associated with this mapping have been loaded
 * @param cacheResponseHandler function called after all cache queries have been processed
  
 */
function CacheMapping(options) {
    this.name = options.name;
    this.values = options.values == null ? [] : options.values;
    this.onStoreInCache  = options.storeInCacheHandler == null ? function() {} : options.storeInCacheHandler;
    this.onCacheResponse = options.cacheResponseHandler == null ? function() {} : options.cacheResponseHandler;
    
}