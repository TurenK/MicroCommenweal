<?php
	require_once('./db.php');
	require_once('./response.php');

	// 创建连接
	$conn = db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);
	//test
	$arr = array("activitySponsor"=>1,"activityName"=> "玉福敬老院","activityImage"=>"aaaa",
	"activityDeadline"=>"2018-10-01" ,"activityTime"=>"2018-10-02","activityAddress"=>"玉福敬老院" ,"activityIntroduction"=>"xxxxxxxxx",
	"aNeedNumOfPerson"=>30,"aSurplusQuota"=>3 ,"activityStatus"=>1 ,"activityRemarks"=>"备注" );
	
	$activitySponsor = $arr['activitySponsor'];
	$activityName = $arr['activityName'];
	$activityImage = $arr['activityImage'];
	$activityDeadline = $arr['activityDeadline'];
	$activityTime = $arr['activityTime'];
	$activityAddress = $arr['activityAddress'];
	$activityIntroduction = $arr['activityIntroduction'];
	$aNeedNumOfPerson = $arr['aNeedNumOfPerson'];
	$aSurplusQuota = $arr['aSurplusQuota'];
	$activityStatus = $arr['activityStatus'];
	$activityRemarks = $arr['activityRemarks'];

	$sql = "INSERT INTO activity 
	(activitySponsor,activityName,activityImage,activityDeadline,activityTime,activityAddress,activityIntroduction,aNeedNumOfPerson,
	aSurplusQuota,activityStatus,activityRemarks)
	VALUES ( '".$activitySponsor."' , '".$activityName."' ,'".$activityImage."', 
	 '".$activityDeadline."' ,'".$activityTime."', '".$activityAddress."' , '".$activityIntroduction."' ,'".$aNeedNumOfPerson."' , '".$aSurplusQuota."','".$activityStatus."','".$activityRemarks."')";

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