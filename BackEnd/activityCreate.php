<?php
	require_once('./db.php');
	require_once('./response.php');

	// 创建连接
	$connect = db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);
	//test
	//$arr = array("activitySponsor"=>1,"activityType"=>"篮球","activityName"=> "篮球赛a",
	//"activityImage"=>"lujing","activityDeadline"=>"2018-10-01" ,"activityStartTime"=>"2018-10-02","activityEndTime"=>"2018-10-02","activityAddress"=>"玉福敬老院" ,
	//"activityTel"=>"2333333","activityIntroduction"=>"xxxxxxxxx",
	//"aNeedNumOfPerson"=>30);
	
	$activitySponsor = $arr['activitySponsor'];
	$activityType = $arr['activityType'];
	$activityName = $arr['activityName'];
//	return response::show(200, 'true', $arr);
	$activityImage = $arr['activityImage'];
	$activityDeadline = $arr['activityDeadline'];
	$activityStartTime = $arr['activityStartTime'];
	$activityEndTime = $arr['activityEndTime'];
	$activityAddress = $arr['activityAddress'];
	$activityTel = $arr['activityTel'];
	$activityIntroduction = $arr['activityIntroduction'];
	$aNeedNumOfPerson = $arr['aNeedNumOfPerson'];

	$find_repeat = "select * from activity where activitySponsor='".$activitySponsor."' and activityName='".$activityName."'";
	$result = mysqli_query ( $connect, $find_repeat );

	if(mysqli_num_rows($result) != 0){
		return Response::show(405, 'false',"data repeat");
	}else if(mysqli_num_rows($result) == 0){
		$sql = "INSERT INTO activity 
		(activitySponsor,activityType,activityName,activityImage,activityDeadline,activityStartTime,
		activityEndTime,activityAddress,activityTel,activityIntroduction,aNeedNumOfPerson,aSurplusQuota,activityAttention)
		VALUES ( '".$activitySponsor."' , '".$activityType."', '".$activityName."' , '".$activityImage."' , 
		 '".$activityDeadline."' ,'".$activityStartTime."', '".$activityEndTime."',
		 '".$activityAddress."' , '".$activityTel."','".$activityIntroduction."' ,
		 '".$aNeedNumOfPerson."','".$aNeedNumOfPerson."',0)";

		if ($connect->query($sql) === TRUE) {
			return response::show(200, 'true', "success");
		} else {
			return response::show(400, 'false', "data deliver fail");
		}
	}
	$connect->close();
?> 