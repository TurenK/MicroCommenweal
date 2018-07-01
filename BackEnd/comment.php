<?php
header("Content-type:text/html;charset=utf8");
	require_once('./db.php');
	require_once('./response.php');
	$conn = Db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);

	$userId = $arr['userId'];
	$activityId = $arr['activityId'];
	$remarks = $arr['remarks'];
	//test
	//$userId = 1;
	//$activityId = 1;
	//$remarks = "2";
	
	$sql = "INSERT INTO participate (userId, activityId, remarks)
	VALUES ( '".$userId."' , '".$activityId."' ,'".$remarks."')";
	
	if ($conn->query($sql) === TRUE) {
		return response::show(200,'true','success');
	} else {
		return response::show(400,'false','data deliver fail');
	}

	$conn->close();
?> 