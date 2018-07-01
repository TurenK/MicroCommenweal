<?php
	require_once('./db.php');
	require_once('./response.php');
	$connect = Db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);

	/* test*/
/*	$arr = array("groupName" => "aliceHelp","groupMail" => "1475",
	"groupPassword" => "xasx","groupAddress" => "youre",
	"groupIntro" => "youre","groupType" => "youre");
	*/
	

	$groupName = $arr['groupName'];
	$groupMail = $arr['groupMail'];
	$groupPassword = $arr['groupPassword'];
	$groupAddress = $arr['groupAddress'];
	$groupIntro = $arr['groupIntro'];
	$groupType = $arr['groupType'];
	$groupImage = $arr['groupImage'];
//	return response::show(200,'true',$arr);
//	echo $arr;

	$sql = "select userId from user where userName = '".$groupName."'";
	$result = $connect->query($sql);
	
	if ($result->num_rows > 0) {
		return response::show(406,'false','this name is a user');
	} else {
		$find_repeat = "select * from groups where groupName='".$groupName."'";
		$result = mysqli_query ( $connect, $find_repeat );
		if(mysqli_num_rows($result) != 0){
			return response::show(405,'false','the data is registered');
		}else{
	//		$sql = "INSERT INTO groups (groupName) VALUES ( '".$groupName."' )";
	//		return response::show(200,'true',$sql);
			$sql = "INSERT INTO groups 
			(groupName, groupmail, groupPassword, groupAddress, groupIntro, groupType, groupImage) 
			VALUES ( '".$groupName."' , '".$groupMail."' ,'".$groupPassword."' ,
			 '".$groupAddress."' , '".$groupIntro."' , '".$groupType."' , '".$groupImage."' )";
			 


			if ($connect->query($sql) === TRUE) {
				return response::show(200,'true',"group registered success");
			} else {
				return response::show(400,'false',"data deliver fail");
			}
		}
	}
	
	$connect->close();
?> 