<?php
	require_once('./db.php');
	require_once('./response.php');
	$connect = Db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);
	
	$groupId = $arr['groupId'];
	
	$sql="UPDATE groups SET groupVerify=1 where groupId='".$groupId."'";
	
	if ($connect->query($sql) === TRUE) {
			return response::show(200, 'true', "success");
		} else {
			return response::show(400, 'false', "data deliver fail");
		}
	
	$connect->close();
?>