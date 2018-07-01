<?php
	require_once('./db.php');
	require_once('./response.php');
	$connect = Db::connect();
    
	$json = file_get_contents ( 'php://input' );  
	$obj = json_decode ( $json,true ); 
	
	$activityId = $obj['activityId'];
	$userId = $obj['userId'];
	$groupId = $obj['groupId'];
	//test
	//$activityId="1";
	//$userId="1";
	
	//userStatus
	$userStatus=1;//已报名
	$query_userStatus="select * from participate where activityId='".$activityId."' and userId='".$userId."'";
	$result=mysqli_query($connect, $query_userStatus);
	if(mysqli_num_rows($result) == 0){
		$userStatus=0;
	}
	//收藏
	$collectStatus=1;
	if(is_null($groupId)){
	$query_collectStatus="select * from starUser where activityId='".$activityId."' and userid='".$userId."'";
	$result=mysqli_query($connect, $query_collectStatus);
	if(mysqli_num_rows($result) == 0){
		$collectStatus=0;
	}
	}else{
		$query_collectStatus="select * from starGroup where activityId='".$activityId."' and groupId='".$groupId."'";
	$result=mysqli_query($connect, $query_collectStatus);
	if(mysqli_num_rows($result) == 0){
		$collectStatus=0;
	}
	}
	
	// 获取发起者信息
	$groupInfo = array();
	$rows=array();
	$query_groupInfo="select groupName, groupmail, groupTelephone, groupIntro, groupAddress, groupCreateTime 
	from groups, activity
	where activity.activitySponsor = groups.groupId and
	activityId= ".(int)$activityId;
	$result=mysqli_query($connect, $query_groupInfo);
	if(mysqli_num_rows($result) !== 0){
		$row_groupInfo = mysqli_fetch_row($result);
		$groupInfo = array(
			'groupName'=>$row_groupInfo[0],
			'groupmail'=>$row_groupInfo[1],
			'groupTelephone'=>$row_groupInfo[2],
			'groupIntro'=>$row_groupInfo[3], 
			'groupAddress'=>$row_groupInfo[4], 
			'groupCreateTime'=>$row_groupInfo[5]
		);
	}
	
	$rows['groupinfo'] = $groupInfo;
	
	//activity information
	$activity = "select activityId,activitySponsor,activityType,activityName,activityImage,activityReleaseTime,activityDeadline,
	activityStartTime, activityEndTime, activityAddress, activityTel, activityIntroduction, aNeedNumOfPerson, aSurplusQuota, activityStatus 
	from activity where activityId = '".$activityId."'";
	$result=mysqli_query($connect, $activity);
	$rows0=array();
	while ( $row = mysqli_fetch_row($result)) {
		$rows0=array('activityId'=>$row[0],
        'activitySponsor'=>$row[1],
		'activityType'=>$row[2],
		'activityName'=>$row[3],
		'activityImage'=>$row[4],
		'activityReleaseTime'=>$row[5],
		'activityDeadline'=>$row[6],
		'activityStartTime'=>$row[7],
		'activityEndTime'=>$row[8],
		'activityAddress'=>$row[9],
		'activityTel'=>$row[10],
		'activityIntroduction'=>$row[11],
		'aNeedNumOfPerson'=>$row[12],
		'aSurplusQuota'=>$row[13],
		'activityStatus'=>$row[14],
		'userStatus'=>$userStatus,
		'collectStatus'=>$collectStatus
		);
	}
	$rows["value"]=$rows0;
	
	//remarks
	$remarks = "select distinct userId, remarks from participate where activityId = '".$activityId."'"; 
	$result1 = mysqli_query($connect, $remarks);
    $rows1=array();
	$i=0;
	while ($row1 = mysqli_fetch_row($result1)) {
		$i++;
		$eachrow = array(
			'userId' => $row1[0],
			'remarks' => $row1[1]
			);
		$rows1[$i] = $eachrow;
	}
	$rows["remarks"]=(object)$rows1;
	
	//join user
	$join = "select distinct p.userId, u.userImage from participate p, user u where p.activityId = '".$activityId."' and p.userId=u.userId";
	$result2 = mysqli_query($connect, $join);
	$rows2=array();
	$k=0;
	if($result){
	while ($row2 = mysqli_fetch_row($result2)) {
		$k++;
		$eachrow = array(
			'userId' => $row2[0],
			'userImage' => $row2[1]
			);
		$rows2[$k] = $eachrow;
	}
	$rows["join"]=(object)$rows2;
	}
	if($rows){
		return response::show(200,'true',$rows);
		//echo json_encode($rows,JSON_UNESCAPED_UNICODE);
	}else{
		return response::show(400,'false','data deliver fail');
	}	
	exit;
?>