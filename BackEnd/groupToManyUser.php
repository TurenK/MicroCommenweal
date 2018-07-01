<?php
header("Content-type:text/html;charset=utf8");
	require_once('./db.php');
	require_once('./response.php');
	$conn = Db::connect();
	
	//返回组织发起过的活动数量
	
	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);

	$activityId = $arr['aId'];
	$groupId = $arr['gId'];
	$rating = $arr['rating'];
	$num = count($rating);
//	echo $num;
	$j = 1;
//	echo 'mei'. $j-1 . 'you';
//	echo $rating['1']['remarks'];

	//test
	//$activityId = 1;

	for($i = 1; $i <= $num; $i++){
//		echo $rating[$GLOBALS['j']]['remarks'];
//		$GLOBALS['j'] ++;
		$sql = "insert into organization (gId, uId, aId, remarks, starNum)
		values ( '".$groupId."' , '".$rating[$GLOBALS['j']]['uId']."' ,'".$activityId."',
		'".$rating[$GLOBALS['j']]['remarks']."', '".$rating[$GLOBALS['j']]['zan']."')";
		
		if ($conn->query($sql) != TRUE) {
			return response::show(400,'false','data deliver fail');
		}
		$GLOBALS['j'] ++;
	}
	return response::show(200,'true',$j-1 . ' remark(s) insert success');

	$conn->close();
?> 