<?php

	header('Content-Type:text/xml');
	include 'serverInfo.php';

	$hoursId = $_GET['id'];
	
	$doc = new DOMDocument();
	$wrapper = $doc->createElement("wrapper");
	$answer = $doc->createElement("answer");
	
	try {
		$con = new PDO("mysql:host=$dbHost;dbname=$dbName", $dbUsername, $dbPassword);
	
		$deleteHoursStatement = $con->prepare('DELETE FROM hours WHERE hours_id= :hours_id');
		$deleteHoursStatement->bindParam(':hours_id', $hoursId, PDO::PARAM_INT);
		$success = $deleteHoursStatement->execute();
	
		if ($success)
			$answer->setAttribute('message', 'Success');
		else
			$answer->setAttribute('message','Database error');
	} catch (Exception $e) {
		$answer->setAttribute('message', 'Error: '.$e->getMessage());
	}
	
	$wrapper->appendChild($answer);
	$doc->appendChild($wrapper);
	
	print $doc->saveXML();
	
?>