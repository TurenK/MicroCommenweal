<?php
	header('Content-Type:text/html;charset=UTF-8');
	require_once('./db.php');
	require_once('./response.php');
	$connect = Db::connect();

	$user = file_get_contents('php://input');
	$arr = json_decode($user, true);
	//test
//	$arr = array("username" => "wrrie","phone" => "123456789","password" => "xasx");
	
	$username = $arr['username'];
	$phone = $arr['phone'];
	$password = $arr['password'];   
	$imageurl = $arr['imageurl'];  
//	echo $arr;
	$sql = "select groupId from groups where groupName = '".$username."'";
	$result = $connect->query($sql);
	
	if ($result->num_rows > 0) {
		return response::show(410,'false','this name is a group');
	} else {
		$find_repeat = "select * from user where userName='".$username."'";
		$result = mysqli_query ( $connect, $find_repeat );
		if(mysqli_num_rows($result) != 0){
			return response::show(405,'false','the data is repeated');
		}else{
			$sql = "INSERT INTO user (userName, userTelephone, userPassword, userImage)
			VALUES ( '".$username."' , '".$phone."', '".$password."', '".$imageurl."')";

			if ($connect->query($sql) === TRUE) {
				return response::show(200,'true','register success');
			} else {
				return response::show(400,'false','data deliver fail');
			}
		}
		
	}

	$connect->close();
?> 