<?php

	header('Content-Type:text/xml');
	include 'serverInfo.php';

	$name = $_GET['jobName'];
	
	$doc = new DOMDocument();
	$wrapper = $doc->createElement("wrapper");
	$answer = $doc->createElement("answer");
	
	try {
		$con = new PDO("mysql:host=$dbHost;dbname=$dbName", $dbUsername, $dbPassword);
	
		$deleteJobStatement = $con->prepare('DELETE FROM jobs WHERE name= :name');
		$deleteJobStatement->bindParam(':name', $name, PDO::PARAM_STR);
		$success = $deleteJobStatement->execute();
	
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