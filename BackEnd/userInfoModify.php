<?php
	require_once('./db.php');
	require_once('./response.php');
	$connect = Db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);
	
	$userId = $arr['userId'];
	$userName = $arr['userName'];
	$userImage = $arr['userImage'];
	$userIntro = $arr['userIntro'];
	//$userTelephone = $arr['userTelephone'];
	$userGender = $arr['userGender'];
	//test
	//$userId = 1;
	//$userName = "b";
	//$userImage = "xxxx";
	//$userIntro = "热火你是不是的v";
	//$userTelephone = "22222333";
	
	$sql="UPDATE user SET userName='".$userName."', userImage='".$userImage."', userIntro='".$userIntro."', userGender = '".$userGender."' where userId='".$userId."'";
	
	if ($connect->query($sql) === TRUE) {
			return response::show(200, 'true', "success");
		} else {
			return response::show(400, 'false', "data deliver fail");
		}
	
	$connect->close();
?>