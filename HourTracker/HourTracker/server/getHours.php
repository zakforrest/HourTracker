<?php

	header('Content-Type:text/xml');
	include 'serverInfo.php';
	
	$jobName = $_GET['jobName'];
	
	$con = new PDO("mysql:host=$dbHost;dbname=$dbName", $dbUsername, $dbPassword);

	$timeFormat = '\'%k:%i\'';
	$dateFormat = '\'%c/%e/%Y\'';
	
	$returnAttributes = 'TIME_FORMAT(hours.startTime, '.$timeFormat.'), TIME_FORMAT(hours.endTime, '.$timeFormat.'), DATE_FORMAT(hours.date, '.$dateFormat.'), jobs.name, jobs.pay_rate, hours.hours_id';
	$statement = $con->prepare('SELECT '.$returnAttributes.' FROM hours INNER JOIN jobs ON hours.job_id=jobs.job_id WHERE jobs.name= :jobName');
	$statement->bindParam(':jobName', $jobName, PDO::PARAM_STR);
	$statement->execute();

	$doc = new DOMDocument();
	$hoursItems = $doc->createElement("hoursItems");
	
	foreach($statement->fetchAll() as $row) {
		$hoursItem = $doc->createElement("hoursItem");
		
		$hoursItem->setAttribute('hoursId', $row['hours_id']);
		$hoursItem->setAttribute('startTime', $row['TIME_FORMAT(hours.startTime, '.$timeFormat.')']);
		$hoursItem->setAttribute('endTime', $row['TIME_FORMAT(hours.endTime, '.$timeFormat.')']);
		$hoursItem->setAttribute('date', $row['DATE_FORMAT(hours.date, '.$dateFormat.')']);
		$hoursItem->setAttribute('jobName', $row['name']);
		$hoursItem->setAttribute('jobPayrate', $row['pay_rate']);
		
		$hoursItems->appendChild($hoursItem);
	}
	$doc->appendChild($hoursItems);
	
	print $doc->saveXML();
	
?>