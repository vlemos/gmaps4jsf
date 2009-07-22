<%@ taglib uri="http://java.sun.com/jsf/html" prefix="h" %>
<%@ taglib uri="http://java.sun.com/jsf/core" prefix="f" %>
<%@ taglib uri="http://code.google.com/p/gmaps4jsf/" prefix="m" %>
<!DOCTYPE PUBLIC html "-//W3C//DTD Xhtml 1.0 Transitional//EN"
  "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head> 
    <title>Welcome to GMaps4JSF</title> 
    <meta http-equiv="X-UA-Compatible" content="IE=EmulateIE7" />    	
    <script src="http://maps.google.com/maps?file=api&amp;v=2&amp;key=ABQIAAAAxrVS1QxlpJHXxQ2Vxg2bJBQdkFK-tWRbPPQS4ACM1pq_e-PltxQXeyH20wQuqDaQ_6EM5UeGGVpnIw"
      type="text/javascript"></script>
	<style>
		html, body { height: 100% }
	</style>
    </head>
	
	<body onunload="GUnload()">
	<f:view>
    	<h:form id="form">
		  	<div>Addressable Markers</div>
    		<m:map width="90%" height="90%" latitude="24" longitude="15" jsVariable="map1" zoom="2">
    			
    			<m:marker address="Cairo, Egypt"/>						   				
    			
    			<m:marker address="NY, USA" draggable="true">
					<m:eventListener eventName="dragend" jsFunction="markerDragHandler"/>    										
    			    <m:htmlInformationWindow htmlText="Iam a draggable marker, you can drag me"/>					
    			</m:marker>    				
    			
    			<m:marker address="Vienna, Austria"/>				
    		
    		</m:map>

		    <script>
		   	function markerDragHandler(latlng) {
		   		alert("Current marker latlng is: " + latlng);  	
		   	}			    
		   	</script>    		

    	</h:form>
	</f:view>
	<%@include file="../templates/footer.jspf" %>   	
    </body>
</html>  
