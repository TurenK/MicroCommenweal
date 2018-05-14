<?php
	require_once('./db.php');
	require_once('./response.php');
	$conn = Db::connect();
	/*
	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);
	*/
	// 这是我自己测试用的    
	$arr = array("userId" => 1,"activityId" => 1);

	$userId = $arr['userId'];
	echo $userId;
	$activityId = $arr['activityId'];
	echo $activityId; 

	$sql = "INSERT INTO participate (userId, activityId)
	VALUES ( $userId, $activityId);";

	$sql .= "update activity set aSurplusQuota = aSurplusQuota - 1
	where activityId = $activityId";


	$response = array("code"=>"404","mseeage"=>"failed","data"=>"none");

	if ($conn->multi_query($sql) === TRUE) {
		$response['code'] = 200;
		$response['mseeage'] = "success";
		$response['data'] = "apply join success";
		echo json_encode($response);
	} else {
		$response['data'] = "Error: " . $sql . "<br>" . $conn->error;
		echo json_encode($response);
	}

	$conn->close();
?> 