<?php 

	header('Content-Type:text/xml');
	include 'serverInfo.php';
	
	$startTime = $_POST['startTime'];
	$endTime = $_POST['endTime'];
	$date = $_POST['date'];
	$jobName = $_POST['jobName'];
	
	//Create mySql time/date strings from php strings
	$mysqlStartTime = date("H:i", strtotime($startTime));
	$mysqlEndTime = date("H:i", strtotime($endTime));
	//http://stackoverflow.com/questions/12120433/php-mysql-insert-date-format
	$splitDate = split('/', $date);
	$mysqlDate = $splitDate[2].'-'.$splitDate[0].'-'.$splitDate[1];
	
	try {
		$con = new PDO("mysql:host=$dbHost;dbname=$dbName", $dbUsername, $dbPassword);
	
		//Get corresponding jobId
		$getJobIdStatement = $con->prepare('SELECT job_id FROM jobs WHERE name = :jobName');
		$getJobIdStatement->bindParam(':jobName', $jobName, PDO::PARAM_INT);
		$getJobIdStatement->execute();
		$jobRow = $getJobIdStatement->fetch();
		$jobId = $jobRow['job_id'];
		
		//Add to hours
		$addStatement = $con->prepare('INSERT INTO hours (startTime, endTime, date, job_id) VALUES (:startTime, :endTime, :date, :jobId)');
		$addStatement->bindParam(':startTime', $mysqlStartTime, PDO::PARAM_INT);
		$addStatement->bindParam(':endTime', $mysqlEndTime, PDO::PARAM_INT);
		$addStatement->bindParam(':date', $mysqlDate, PDO::PARAM_INT);
		$addStatement->bindParam(':jobId', $jobId, PDO::PARAM_INT);
		$addStatement->execute();
		
		echo 'Success';
	} catch (Exception $e) {
		echo 'Error: '.$e->getMessage();
	}
	
?>