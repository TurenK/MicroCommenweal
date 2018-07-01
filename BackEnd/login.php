<?php
header("Content-type:text/html;charset=utf8");
	require_once('./db.php');
	require_once('./response.php');
	$connect = db::connect();
	 //获取客户端传入的json数据  
	$json = file_get_contents ( 'php://input' );  
	$obj = json_decode ( $json, true ); 
	
	$userName = $obj['userName'];
//	echo $userName;
	$userPassword = $obj['password'];
//	return Response::show(200,'true',$obj);
	//test
//	$userName = 'caiwen';
//	$userPassword = '12345678';
	
	$sql_userlogin = "select * from user where userName='".$userName."' and userPassword='".$userPassword."'";
	$sql_grouplogin = "select * from groups where groupName='".$userName."' and groupPassword='".$userPassword."'";
	$result = mysqli_query ( $connect, $sql_userlogin );
	if(mysqli_num_rows($result) ==1){
		while($row = mysqli_fetch_row($result)) {
			$rows=array(
				'userId'=>$row[0],
				'userName'=>$row[1],
				'userTelephone'=>$row[2],
				'userAge'=>$row[4],
				'userImage'=>$row[5],
				'userLabel'=>$row[6],
				'userAttention'=>$row[7],
				'userType'=>$row[8],
				'userIntro'=>$row[9],
				'userGender'=>$row[10],
				'userVerify'=>(int)$row[11]
			);
		} 
		return Response::show(200,'true',$rows);
		
	}else if(mysqli_num_rows($result) > 1){
		return Response::show(409, 'false','duplicate user');
	}else{
		$result = mysqli_query ( $connect, $sql_grouplogin );
		if(mysqli_num_rows($result) ==1){
			while($row = mysqli_fetch_row($result)) {
				$rows=array(
					'groupId'=>$row[0],
					'groupName'=>$row[1],
					'groupmail'=>$row[2],
					'groupPassword'=>$row[3],
					'groupCreateTime'=>$row[4],
					'groupAddress'=>$row[5],
					'groupIntro'=>$row[6],
					'groupType'=>$row[7],
					'groupAttension'=>$row[8],
					'groupImage'=>$row[9],
					'groupTelephone'=>$row[10],
					'groupVerify'=>(int)$row[11]
				);
			}
			return Response::show(202,'true',$rows);		
		}else if(mysqli_num_rows($result) > 1){
			return Response::show(409, 'false','duplicate user');
		}
		return Response::show(404, 'false','password error');
	}
	mysqli_close($connect);
	exit;
?>