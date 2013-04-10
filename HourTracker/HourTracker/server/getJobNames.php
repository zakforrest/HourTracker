<?php

	//http://net.tutsplus.com/tutorials/php/php-database-access-are-you-doing-it-correctly/

	header('Content-Type:text/xml');
	include 'serverInfo.php';

	$con = new PDO("mysql:host=$dbHost;dbname=$dbName", $dbUsername, $dbPassword);
	
	$statement = $con->prepare('SELECT name FROM jobs');
	$statement->execute();
	
	$doc = new DOMDocument();
	$jobs = $doc->createElement("jobs");
	
	foreach($statement->fetchAll() as $row) {
		$job = $doc->createElement("job");
		$job->setAttribute('jobName', $row['name']);
		$jobs->appendChild($job);
	}
	$doc->appendChild($jobs);
	
	print $doc->saveXML();
	
?>