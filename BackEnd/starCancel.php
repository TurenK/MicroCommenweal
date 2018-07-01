<?php
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
		$sql = "delete from staruser 
		where userId = '".$userId."' and activityId = '".$activityId."' ;";
		$sql .= "update activity set activityAttention = activityAttention - 1
			where activityId = ".$activityId.";";
	}else if( $groupId != null ){
		$sql = "delete from stargroup 
		where groupId = '".$groupId."' and activityId = '".$activityId."' ";
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