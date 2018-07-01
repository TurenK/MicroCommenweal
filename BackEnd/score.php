<?php
	require_once('./db.php');
	require_once('./response.php');
	$conn = Db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);

	$userId = $arr['userId'];
	$score = $arr['score'];
	//test
	//$userId = 31;
	//$activityId = 1;
	
	$sql = "select score from score where userId = '".$userId."' ";
	$result = $conn->query($sql);
	
	if ($result->num_rows > 0) {
		$sql = "update score set score = '".$score."' where userId = '".$userId."' ";
	} else if($result->num_rows == 0){
		$sql = "insert into score (userId, score) 
		values ( '".$userId."', '".$score."' )";
	}
	
	if ($conn->query($sql) === TRUE) {
		return response::show(200,'true','success');
	} else {
		return response::show(400,'false','data deliver fail');
	}

	$conn->close();
?> 