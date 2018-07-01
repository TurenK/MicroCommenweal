<?php
header("Content-type:text/html;charset=utf8");
	require_once('./db.php');
	require_once('./response.php');
	$conn = Db::connect();
	
	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);
	
	// 这是我自己测试用的    
//	$arr = array("userId" => 1,"activityId" => 1);

	$userId = $arr['userId'];
//	echo $userId;
	$activityId = $arr['activityId'];
//	echo $activityId; 

	$sql = "select pId from participate where userId = $userId and activityId = $activityId";
	$result = $conn->query($sql);
	
	if ($result->num_rows > 0) {
		   return response::show(403,'false','you have join the activity');
	} else {

		$sql = "INSERT INTO participate (userId, activityId)
		VALUES ( $userId, $activityId);";

		$sql .= "update activity set aSurplusQuota = aSurplusQuota - 1
		where activityId = $activityId;";

		$sql .= "update activity set activityAttention = activityAttention + 1
			where activityId = $activityId;";
		
		//$response = array("code"=>"404","message"=>"failed","data"=>"none");

		if ($conn->multi_query($sql) === TRUE) {
			return response::show(200,'true','success');
		} else {
			return response::show(400,'false','data deliver fail');
		}
	}
	
	$conn->close();
?> 