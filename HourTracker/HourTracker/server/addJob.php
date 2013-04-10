<?php

	header('Content-Type:text/xml');
	include 'serverInfo.php';
	
	$name = $_POST['name'];
	$payrate = $_POST['payrate'];
	
	try {
		$con = new PDO("mysql:host=$dbHost;dbname=$dbName", $dbUsername, $dbPassword);
	
		$checkDuplicateStatement = $con->prepare('SELECT name FROM jobs WHERE name = :name');
		$checkDuplicateStatement->bindParam(':name', $name, PDO::PARAM_STR);
		$checkDuplicateStatement->execute();
		
		if ($checkDuplicateStatement->rowCount()!=0)
			throw new Exception('Job already exists.');
		
		//Add to jobs
		$addStatement = $con->prepare('INSERT INTO jobs (name, pay_rate) VALUES (:name, :payrate)');
		$addStatement->bindParam(':name', $name, PDO::PARAM_STR);
		$addStatement->bindParam(':payrate', $payrate, PDO::PARAM_INT);
		$addStatement->execute();
	
		echo 'Success';
	} catch (Exception $e) {
		echo 'Error: '.$e->getMessage();
	}
	
?>