<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
    <title>PDFObject example</title>
     <script type="text/javascript" src="js/pdfobject.js"></script>
     <script type="text/javascript">
      window.onload = function (){
        var myPDF = new PDFObject({ url: "${filepath}" }).embed();   
      };
      </script>
</head>
<body>

</body>
</html>