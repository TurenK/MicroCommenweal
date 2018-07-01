<?php
	require_once('./db.php');
	require_once('./response.php');
	$connect = Db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);
	
	$groupId = $arr['groupId'];
	$groupName = $arr['groupName'];
	$groupImage = $arr['groupImage'];
	$groupIntro = $arr['groupIntro'];
	//$groupTelephone = $arr['groupTelephone'];
	//test
	//$groupId = 1;
	//$groupName = "b";
	//$groupImage = "xxxx";
	//$groupIntro = "ssssssssss";
	//$groupTelephone = "22222333";
	
	$sql="UPDATE groups SET groupName='".$groupName."', groupImage='".$groupImage."', groupIntro='".$groupIntro."' where groupId='".$groupId."'";
	
	if ($connect->query($sql) === TRUE) {
			return response::show(200, 'true', "success");
		} else {
			return response::show(400, 'false', "data deliver fail");
		}
	
	$connect->close();
?>