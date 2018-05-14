<?php
	require_once('./db.php');
	require_once('./response.php');
	$conn = Db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);

	/* test*/
	$arr = array("groupName" => "youre","groupMail" => "1475",
	"groupPassword" => "xasx","groupAddress" => "youre",
	"groupIntro" => "youre","groupType" => "youre");

	$groupName = $arr['groupName'];
	$groupMail = $arr['groupMail'];
	$groupPassword = $arr['groupPassword'];
	$groupAddress = $arr['groupAddress'];
	$groupIntro = $arr['groupIntro'];
	$groupType = $arr['groupType'];


	$sql = "INSERT INTO groups 
	(groupName, groupmail, groupPassword, groupAddress, groupIntro, groupType) 
	VALUES ( '".$groupName."' , '".$groupMail."' ,'".$groupPassword."' ,
	 '".$groupAddress."' , '".$groupIntro."' , '".$groupType."' )";

	$response = array("code"=>"404","mseeage"=>"failed","data"=>"none");

	if ($conn->query($sql) === TRUE) {
		$response['code'] = 200;
		$response['mseeage'] = "success";
		$response['data'] = "group registered success";
		echo json_encode($response);
	} else {
		$response['data'] = "Error: " . $sql . "<br>" . $conn->error;
		echo json_encode($response);
	}

	$conn->close();
?> 