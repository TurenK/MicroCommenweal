<?php
	require_once('./db.php');
	require_once('./response.php');
	$connect = Db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);
	
	$userId = $arr['userId'];
	
	$sql="UPDATE user SET userVerify=1 where userId='".$userId."'";
	
	if ($connect->query($sql) === TRUE) {
			return response::show(200, 'true', "success");
		} else {
			return response::show(400, 'false', "data deliver fail");
		}
	
	$connect->close();
?>