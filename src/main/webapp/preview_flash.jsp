<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <script type="text/javascript" src="js/jquery.min.js"></script>
    <script type="text/javascript" src="js/flexpaper.js"></script>
    <script type="text/javascript" src="js/flexpaper_handlers.js"></script>
	<style type="text/css" media="screen">   
	            html, body  { height:100%; }  
	            body { margin:0; padding:0; overflow:auto; }     
	            #flashContent { display:none; }  
	 </style>   
<title>preview </title>
</head>
<body>
      	<div style="position:absolute;left:10px;top:10px;">
	        <a id="documentViewer" style="width:680px;height:480px;display:block"></a>
	        
	        <script type="text/javascript"> 
	        $('#documentViewer').FlexPaperViewer(
	                { config : {

	                    SWFFile : '${filepath}',

	                    Scale : 0.6,
	                    ZoomTransition : 'easeOut',
	                    ZoomTime : 0.5,
	                    ZoomInterval : 0.2,
	                    FitPageOnLoad : true,
	                    FitWidthOnLoad : false,
	                    FullScreenAsMaxWindow : false,
	                    ProgressiveLoading : false,
	                    MinZoomSize : 0.2,
	                    MaxZoomSize : 5,
	                    SearchMatchAll : false,
	                    InitViewMode : 'Portrait',
	                    RenderingOrder : 'flash',
	                    StartAtPage : '',

	                    ViewModeToolsVisible : true,
	                    ZoomToolsVisible : true,
	                    NavToolsVisible : true,
	                    CursorToolsVisible : true,
	                    SearchToolsVisible : true,
	                    WMode : 'window',
	                    localeChain: 'en_US'
	                }}
	        );
	        </script>
	        
	           
        </div>  
	</body>  
</html>  
