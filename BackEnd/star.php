<?php
header("Content-type:text/html;charset=utf8");
	require_once('./db.php');
	require_once('./response.php');
	$conn = Db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);

	$userId = $arr['userId'];
	$groupId = $arr['groupId'];
	$activityId = $arr['activityId'];
	//test
	//$userId = 31;
	//$groupId = 1;
	//$activityId = 1;
	
	if( $userId != null ){
		$sql = "INSERT INTO staruser (userId, activityId)
			VALUES ( '".$userId."' , '".$activityId."' );";
		$sql .= "update activity set activityAttention = activityAttention + 1
			where activityId = ".$activityId.";";
	}else if( $groupId != null ){
		$sql = "INSERT INTO stargroup (groupId, activityId)
			VALUES ( '".$groupId."' , '".$activityId."' );";
	}else{
		return Response::show(400, 'false', 'no star');
	}
	
	if ($conn->multi_query($sql) === TRUE) {
		return response::show(200,'true','success');
	} else {
		return response::show(400,'false','data deliver fail');
	}

	$conn->close();
?> 