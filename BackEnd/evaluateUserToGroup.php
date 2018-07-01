<?php
header("Content-type:text/html;charset=utf8");
	require_once('./db.php');
	require_once('./response.php');
	$conn = Db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);

	$userId = $arr['userId'];
	$activityId = $arr['actId'];
	$cominfo = $arr['cominfo'];
	$ratnum = $arr['ratnum'];
	//test
	//$userId = 31;
	//$activityId = 1;
	//$cominfo = "younimademaobing";
	//$ratnum = 1;
	
	$sql = "INSERT INTO evaluateUserToGroup (userId, activityId, cominfo,ratnum)
	VALUES ( '".$userId."' , '".$activityId."' ,'".$cominfo."','".$ratnum."')";
	
	if ($conn->query($sql) === TRUE) {
		return response::show(200,'true','success');
	} else {
		return response::show(400,'false','data deliver fail');
	}

	$conn->close();
?> 