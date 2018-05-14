<?php
	require_once('./db.php');
	require_once('./response.php');
	$connect = Db::connect();

	$user = file_get_contents('php://input');
//	echo $user;  //测试用
	$arr = json_decode($user, true);
//	echo var_dump($arr['username']);  //测试用
	
	$arr = array("username" => "youre","phone" => "1475","password" => "xasx");
	
	$username = $arr['username'];
	$phone = $arr['phone'];
	$password = $arr['password'];
	//var_dump(json_decode($user, true));

	// 这是我自己测试用的    
	

	$sql = "INSERT INTO user (userName, userTelephone, userPassword)
	VALUES ( '".$username."' , $phone, '".$password."')";

	$response = array("code"=>"404","mseeage"=>"failed","data"=>"none");

	if ($connect->query($sql) === TRUE) {
		return Response::show(200, 'register success');
	} else {
		$response['data'] = "Error: " . $sql . "<br>" . $connect->error;
		echo json_encode($response);
	}

	$connect->close();
?> 