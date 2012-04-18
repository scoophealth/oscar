(function(className){
var $ = {

    /** No SWF Object Multiple File Upload
     * @version         1.1.1
     * @compatibility   Chrome 1, FireFox 3+, Internet Explorer 5+, Opera 8+, Safari 3+
     * @author          Andrea Giammarchi
     * @blog            webreflection.blogspot.com
     * @license         Mit Style License
     */

    /**
     * @description load dedicated CSS (avoid CSS download for browsers with JavaScript disabled)
     * @param       String      main noswfupload css file (the wrap)
     * @param       String      optional icons file (the list inside the wrap)
     * @return      Object      noswfupload itself
     */
    css:function(css, icons){
        var head    = document.getElementsByTagName("head")[0] || document.documentElement,
            style   = document.createElement("link");
        style.setAttribute("rel", "stylesheet");
        style.setAttribute("type", "text/css");
        style.setAttribute("media", "all");
        style.setAttribute("href", css);
        head.insertBefore(style, head.firstChild);
        return  icons ? $.css(icons) : $;
    },

    // DOM event add, del, stop Helper
    event:{

        /**
         * @description add an event via addEventListener or attachEvent
         * @param       DOMElement  the element to add event
         * @param       String      event name without "on" (e.g. "mouseover")
         * @param       Function    the callback to associate as event
         * @return      Object      noswfupload.event
         */
        add:document.addEventListener ?
            function(node, name, callback){node.addEventListener(name, callback, false);return  this;}:
            function(node, name, callback){node.attachEvent("on" + name, callback);return  this;},

        /**
         * @description remove an event via removeEventListener or detachEvent
         * @param       DOMElement  the element to remove event
         * @param       String      event name without "on" (e.g. "mouseover")
         * @param       Function    the callback associated as event
         * @return      Object      noswfupload.event
         */
        del:document.removeEventListener ?
            function(node, name, callback){node.removeEventListener(name, callback, false);return  this;}:
            function(node, name, callback){node.detachEvent("on" + name, callback);return  this;},

        /**
         * @description to block event propagation and prevent event default
         * @param       void        generated event or undefined
         * @return      Boolean     false
         */
        stop:function(e){
            if(!e) {
                if(self.event)
                    event.returnValue = !(event.cancelBubble = true);
            } else {
                e.stopPropagation ? e.stopPropagation() : e.cancelBubble = true;
                e.preventDefault ? e.preventDefault() : e.returnValue = false;
            };
            return  false;
        }
    },

    /**
     * @description to add events to the wrap object, mainly for internal uses
     * @param       Object      generated wrap object
     * @return      Object      the wrap object itself
     */
    events:function(wrap){
        $.event.add(wrap.dom.input, "change", function(){
            lastInput = wrap.dom.input;
            $.event.del(wrap.dom.input, "change", arguments.callee);
            for(var max = false, input = wrap.dom.input.cloneNode(true), i = 0, files = $.files(wrap.dom.input); i < files.length; i++){
                 if(!files.item(i).value){
           if(!files.item(i).fileName) files.item(i).fileName=files.item(i).name;
           if(!files.item(i).fileSize) files.item(i).fileSize=files.item(i).size;
       }
               var value   = files.item(i).fileName || (files.item(i).fileName = files.item(i).value.split("\\").pop()),
                    ext     = -1 !== value.indexOf(".") ? value.split(".").pop().toLowerCase() : "unknown";
                if(wrap.fileType && -1 === wrap.fileType.indexOf("*." + ext)){
                    max = true;
                    $.text(
                        wrap.dom.info,
                        $.lang.errorType
                            .replace(/{fileType}/g, wrap.fileType)
                            .replace(/{fileName}/g, value)
                    );
                } else if(wrap.maxSize !== -1 && files.item(i).fileSize && wrap.maxSize < files.item(i).fileSize){
                    max = true;
                    $.text(
                        wrap.dom.info,
                        $.lang.errorSize
                            .replace(/{maxSize}/g, $.size(wrap.maxSize))
                            .replace(/{fileName}/g, value)
                            .replace(/{fileSize}/g, $.size(files.item(i).fileSize))
                    );
                } else {
                    var li  = document.createElement("li"),
                        a   = li.appendChild(document.createElement("a"));
                    wrap.files.unshift(files.item(i));
                    a.href = "#" + value;
                    a.alt = a.title = (files.item(i).fileSize ? "[" + $.size(files.item(i).fileSize) + "] " : "") + value;
                    li.className = ext;
                    $.text(a, value);
                    $.event
                        .add(a, "click", $.empty)
                        .add(a, "dblclick", files.item(i).remove = (function(li){
                            return function(e){
                                for(var i = 0, childNodes = li.parentNode.getElementsByTagName("li"); i < childNodes.length; i++){
                                    if(childNodes[i] === li){
                                        var value = wrap.files[i].fileName;
                                        $.event
                                            .del(li, "click", $.empty)
                                            .del(li, "dblclick", arguments.callee)
                                        ;
                                        li.parentNode.removeChild(li);
                                        li = a = null;
                                        wrap.files.splice(i, 1);
                                        $.text(wrap.dom.info, $.lang.removedFile.replace(/{fileName}/g, value));
                                        break;
                                    }
                                };
                                return  $.empty(e);
                            };
                        })(li))
                    ;
                    if(typeof files.item(i).fileSize != "number")
                        files.item(i).fileSize = -1;
                    if(wrap.dom.ul.firstChild)
                        wrap.dom.ul.insertBefore(li, wrap.dom.ul.firstChild);
                    else
                        wrap.dom.ul.appendChild(li);
                };
            };
            input.value = "";
            wrap.dom.input.parentNode.replaceChild(input, wrap.dom.input);
            wrap.dom.input  = input;
            if(!max)
                $.text(wrap.dom.info, $.lang.removeFile);
            $.event.add(wrap.dom.input, "change", arguments.callee);
        });
        return  wrap;
    },

    /**
     * @description used as empty event, mainly for internal uses
     * @param       void        generated event or undefined
     * @return      Boolean     false
     */
    empty:function(e){return $.event.stop(e);},

    /**
     * @description normalize input.files. create if not present, add item method if not present
     * @param       Object      generated wrap object
     * @return      Object      the wrap object itself
     */
    files:(function(item){
        return function(input){
            var files = input.files || [input];
            if(!files.item)
                files.item = item;
            return  files;
        };
    })(function(i){return this[i];}),

    // showed message, change for local language messages
    // brackets properties are replaced
    lang:{

        // after one or more file has been added
        removeFile:"Double click to remove a file",

        // after a file has been removed
        removedFile:"File {fileName} removed",

        // file over maxSize parameter
        errorSize:"WARNING - Maximum size is {maxSize}. File {fileName} is {fileSize}",

        // file without a valid type
        errorType:"WARNING - File {fileName} has not a valid type: {fileType}"
    },

    /**
     * @description send a single file via XMLHttpRequest, if possible, or iframe in every other case.
     * @param       Object      handler used to perform operations (for example, a wrap Object)
     * @param       Number      optional file maxSize
     * @return      Object      the handler itself
     */
    sendFile:(function(toString){
        var multipart = function(boundary, name, file){
                return  "--".concat(
                    boundary, CRLF,
                    'Content-Disposition: form-data; name="', name, '"; filename="', file.fileName, '"', CRLF,
                    "Content-Type: application/octet-stream", CRLF,
                    CRLF,
                    file.getAsBinary(), CRLF,
                    "--", boundary, "--", CRLF
                );
            },
            isFunction  = function(Function){return  toString.call(Function) === "[object Function]";},
            split       = "onabort.onerror.onloadstart.onprogress".split("."),
            length      = split.length,
            CRLF        = "\r\n",
            xhr = this.XMLHttpRequest ? new XMLHttpRequest : new ActiveXObject("Microsoft.XMLHTTP"),
            sendFile;

        // FireFox 3+, Safari 4 beta (Chrome 2 beta file is buggy and will not work)
        if(xhr.upload || xhr.sendAsBinary)
            sendFile = function(handler, maxSize){
                if(-1 < maxSize && maxSize < handler.file.fileSize){
                    if(isFunction(handler.onerror))
                        handler.onerror('file too big');
                    return;
                };
                for(var
                    xhr = new XMLHttpRequest,
                    upload = xhr.upload || {addEventListener:function(event,callback){this["on"+event]=callback}},
                    i = 0;
                    i < length;
                    i++
                )
                    upload.addEventListener(
                        split[i].substring(2),
                        (function(event){
                            return  function(rpe){
                                if(isFunction(handler[event]))
                                    handler[event](rpe, xhr);
                            };
                        })(split[i]),
                        false
                    );
                upload.addEventListener(
                    "load",
                    function(rpe){
                        if(handler.onreadystatechange === false){
                            if(isFunction(handler.onload))
                                handler.onload(rpe, xhr);
                        } else {
                            setTimeout(function(){
                                if(xhr.readyState === 4){
                                    if(xhr.status == 500 && isFunction(handler.onerror)) {
					var errortext = xhr.getResponseHeader('oscar_error');
                                        handler.onerror(errortext);
				    }
                                    else if(isFunction(handler.onload))
                                        handler.onload(rpe, xhr);
                                } else
                                    setTimeout(arguments.callee, 15);
                            }, 15);
                        }
                    },
                    false
                );
                    
                xhr.open("post", handler.url, true);
                if(!xhr.upload){
                    var rpe      = {loaded:0, total:handler.file.fileSize, simulation:true};
                    rpe.interval    = setInterval(function(){
                        rpe.loaded += 1024 / 4;
                        if(rpe.total <= rpe.loaded)
                            rpe.loaded = rpe.total;
                        upload.onprogress(rpe);
                    }, 100);
                    xhr.onabort = function(){
                        upload.onabort({});

                    };
                    xhr.onerror = function(){
                        upload.onerror({});
                    };
                    xhr.onreadystatechange = function(){
                        switch(xhr.readyState){
                            case    2:
                            case    3:
                                if(rpe.total <= rpe.loaded)
                                    rpe.loaded = rpe.total;
                                upload.onprogress(rpe);
                                break;
                            case    4:
                                clearInterval(rpe.interval);
                                rpe.interval = 0;
                                rpe.loaded = rpe.total;
                                upload.onprogress(rpe);
                                upload[199 < xhr.status && xhr.status < 400 ? "onload" : "onerror"]({});
                                break;
                        }
                    };
                    upload.onloadstart(rpe);
                };
                var formdata;
       try{formdata = new FormData();formdata.append(handler.name, handler.file);}catch(e){}
               if(formdata){xhr.send(formdata);}
               else if(handler.file.getAsBinary){
                    var boundary = "AjaxUploadBoundary" + (new Date).getTime();
                    xhr.setRequestHeader("Content-Type", "multipart/form-data; boundary=" + boundary);
                    xhr[xhr.sendAsBinary ? "sendAsBinary" : "send"](multipart(boundary, handler.name, handler.file));
                } else {
                    xhr.setRequestHeader("Content-Type", "multipart/form-data");
                    xhr.setRequestHeader("X-Name", handler.name);
                    xhr.setRequestHeader("X-Filename", handler.file.fileName);
                    xhr.send(handler.file);
                };
                return  handler;
            };
        // Internet Explorer, Opera, others
        else
            sendFile = function(handler, maxSize){
                var url = handler.url.concat(-1 === handler.url.indexOf("?") ? "?" : "&", "AjaxUploadFrame=true"),
                    rpe = {loaded:1, total:100, simulation:true, interval:setInterval(function(){
                        if(rpe.loaded < rpe.total)
                            ++rpe.loaded;
                        if(isFunction(handler.onprogress))
                            handler.onprogress(rpe, {});
                    }, 100)},
                    onload  = function(){
                        iframe.onreadystatechange = iframe.onload = iframe.onerror = null;
                        form.parentNode.removeChild(form);
                        form = null;
                        clearInterval(rpe.interval);
                        rpe.loaded = rpe.total;
                        try {
                            var responseText = (iframe.contentWindow.document || iframe.contentWindow.contentDocument).body.innerHTML;
                            if(isFunction(handler.onprogress))
                                handler.onprogress(rpe, {});
                            if(isFunction(handler.onload))
                                handler.onload(rpe, {responseText:responseText});
                        } catch(e) {
                            if(isFunction(handler.onerror))
                                handler.onerror(rpe, event || window.event);
                        }
                    },
                    target  = ["AjaxUpload", (new Date).getTime(), String(Math.random()).substring(2)].join("_");
                    
                try { // IE < 8 does not accept enctype attribute ...
                    var form    = document.createElement('<form enctype="multipart/form-data"></form>'),
                        iframe  = handler.iframe || (handler.iframe = document.createElement('<iframe id="' + target + '" name="' + target + '" src="' + url + '"></iframe>'));
                } catch(e) {
                    var form    = document.createElement('form'),
                        iframe  = handler.iframe || (handler.iframe = document.createElement("iframe"));
                    form.setAttribute("enctype", "multipart/form-data");
                    iframe.setAttribute("name", iframe.id = target);
                    iframe.setAttribute("src", url);
                };
                iframe.style.position = "absolute";
                iframe.style.left = iframe.style.top = "-10000px";
                iframe.onload = onload;
                iframe.onerror = function(event){
                    if(isFunction(handler.onerror))
                        handler.onerror(rpe, event || window.event);
                };
                iframe.onreadystatechange = function(){
                    if(/loaded|complete/i.test(iframe.readyState))
                        onload();
                    else if(isFunction(handler.onloadprogress)){
                        if(rpe.loaded < rpe.total)
                            ++rpe.loaded;
                        handler.onloadprogress(rpe, {readyState:{loading:2,interactive:3,loaded:4,complete:4}[iframe.readyState] || 1});
                    }
                };
                form.setAttribute("action", handler.url);
                form.setAttribute("target", iframe.id);
                form.setAttribute("method", "post");
                form.appendChild(handler.file);
                form.style.display = "none";
                if(isFunction(handler.onloadstart))
                    handler.onloadstart(rpe, {});
                with(document.body || document.documentElement){
                    appendChild(iframe);
                    appendChild(form).submit();
                };
                return  handler;
            };
        xhr = null;
        return  sendFile;
    })(Object.prototype.toString),

    /**
     * @description send a collection of files via sendFile method
     * @param       Object      handler used to perform operations (for example, a wrap Object)
     * @param       Number      optional file maxSize
     * @return      Object      the handler itself
     */
    sendFiles:function(handler, maxSize){
        var length = handler.files.length,
            i = 0,
            onload = handler.onload,
            onloadstart = handler.onloadstart;
        handler.current = 0;
        handler.total = 0;
        handler.sent = 0;
        while(handler.current < length)
            handler.total += handler.files[handler.current++].fileSize;
        handler.current = 0;
        if(length && handler.files[0].fileSize !== -1){
            handler.file = handler.files[handler.current];
            $.sendFile(handler, maxSize).onload = function(rpe, xhr){
                handler.onloadstart = null;
                handler.sent += handler.files[handler.current].fileSize;
                if(++handler.current < length){
                    handler.file = handler.files[handler.current];
                    $.sendFile(handler, maxSize).onload = arguments.callee;
                } else if(onload) {
                    handler.onloadstart = onloadstart;
                    handler.onload = onload;
                    handler.onload(rpe, xhr);
                }
            };
        } else if(length) {
            handler.total = length * 100;
            handler.file = handler.files[handler.current];
            $.sendFile(handler, maxSize).onload = function(rpe, xhr){
                var callee = arguments.callee;
                handler.onloadstart = null;
                handler.sent += 100;
                if(++handler.current < length){
                    if(/\b(chrome|safari)\b/i.test(navigator.userAgent)){
                        handler.iframe.parentNode.removeChild(handler.iframe);
                        handler.iframe = null;
                    };
                    setTimeout(function(){
                        handler.file = handler.files[handler.current];
                        $.sendFile(handler, maxSize).onload = callee;
                    }, 15);
                } else if(onload) {
                    setTimeout(function(){
                        handler.iframe.parentNode.removeChild(handler.iframe);
                        handler.iframe = null;
                        handler.onloadstart = onloadstart;
                        handler.onload = onload;
                        handler.onload(rpe, xhr);
                    }, 15);
                }
            };
        };
        return  handler;
    },

    /**
     * @description return a human friendly size
     * @param       Number      total bytes
     * @return      String      human friendly size (e.g 1024 === "1.0 Kb")
     */
    size:(function(info){
        return function(bytes){
            var i = 0;
            while(1023 < bytes){
                bytes /= 1024;
                ++i;
            };
            return  (i ? bytes.toFixed(2) : bytes) + info[i];
        };
    })([" bytes", " Kb", " Mb", " Gb", " Tb"]),

    /**
     * @description add one or more textnode into a DOM element after removing every node, mainly for internal uses.
     * @param       DOMElement  the node to populate with one or more textnodes
     */
    text:(function(slice){
        return function(node){
            arguments = slice.call(arguments, 1);
            while(node.firstChild)
                node.removeChild(node.firstChild);
            while(arguments.length){
                node.appendChild(document.createTextNode(arguments.shift()));
                if(arguments.length)
                    node.appendChild(document.createElement("br"));
            };
        };
    })(Array.prototype.slice),

    /**
     * @description create the wrap node to contain the multiple file upload application.
     *              Return the wrap object to use for every upload operation.
     * @param       DOMElement  the input type file to wrap to obtain a noswfupload component.
     * @param       Number      optional maxSize for each selected file
     */
    wrap:function(input, maxSize){
        var wrap    = document.createElement("div"),
            total   = document.createElement("span"),
            current = total.cloneNode(true),
            ul      = document.createElement("ul"),
            info    = total.cloneNode(true)
        ;

        // be sure input accept multiple files
        input.setAttribute("multiple", "multiple");
        input.setAttribute("style", "background:#F2F7FF");

        // created structure swapped with the input
        // <div class="noswfupload">
        //     <input type="file" multiple="multiple" name="{fileName}" />
        //     <span class="total"></span>
        //     <span class="current"></span>
        //     <ul>[
        //         <li class="{fileExtension}">{fileName}</li>
        //     ]</ul>
        //     <span class="info">{customInfo}</span>
        // </div>
        with(wrap){
            appendChild(total);
            appendChild(current);
            appendChild(ul);
            appendChild(info);
        };

        wrap.className  = className;
        total.className = "total";
        current.className = "current";
        info.className = "info";
        total.style.visibility = current.style.visibility = "hidden";
        wrap.insertBefore(input.parentNode.replaceChild(wrap, input), total);
        input.value = "";

        // wrap Object
        return  $.events({

            // DOM namespace
            dom:{
                wrap:wrap,          // wrap div
                input:input,        // input file
                total:total,        // total bar
                current:current,    // current bar
                ul:ul,              // list of files
                info:info,          // custom info
                disabled:false      // internal use, checks input file state
            },
            name:input.name,        // name to send for each file ($_FILES[{name}] in the server)
                                    // maxSize is the maximum amount of bytes for each file
            maxSize:maxSize ? maxSize >> 0 : -1,
            files:[],               // file list

            // remove every file from the noswfupload component
            clean:function(){
                while(this.files.length)
                    this.files[0].remove();
            },

            // upload one file a time (which make progress possible rather than all files in one shot)
            // the handler is an object injected into the wrap one, could be the wrap itself or
            // something like {onload:function(){alert("OK")},onerror:function(){alert("Error")}, etc ...}
            upload:function(handler){
                if(handler){
                    for(var key in handler)
                        this[key] = handler[key];
                };
                $.sendFiles(this, this.maxSize);
                return  this;
            },

            // hide progress bar (total + current) and enable files selection
            hide:function(){
                if(this.dom.disabled){
                    this.dom.disabled = false;
                    this.dom.input.removeAttribute("disabled");
                };
                total.style.visibility = current.style.visibility = "hidden";
            },

            // show progress bar and disable file selection (used during upload)
            // total and current are pixels used to style bars
            // totalProp and currentProp are properties to change, "height" by default
            show:function(total, current, totalProp, currentProp){
                var tstyle = this.dom.total.style,
                    cstyle = this.dom.current.style;
                if(!this.dom.disabled){
                    this.dom.disabled = true;
                    this.dom.input.setAttribute("disabled", "disabled");
                    tstyle.visibility = cstyle.visibility = "";
                };
                tstyle[totalProp || "height"] = (total >> 0) + "px";
                cstyle[currentProp || "height"] = (current >> 0) + "px";
            }
        });
    }
};

// assign the global object
this[className]=$})("noswfupload");
