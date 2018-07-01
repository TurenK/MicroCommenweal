<?php
	header("Content-type:text/html;charset=utf8");
	require_once('./db.php');
	require_once('./response.php');
	require_once('./history.php');
	$connect = db::connect();

	$json = file_get_contents('php://input');
	$arr = json_decode($json, true);
	$userId = $arr['userId'];

//$userId = 1;//测试

	$sql = "select activity.activityId,activity.activityName,activity.activityImage,activity.aSurplusQuota
				,activity.activityStatus,activity.aNeedNumOfPerson
				from activity, participate,user 
				where participate.userId =".$userId." and participate.activityId = activity.activityId 
				group by activity.activityId 
				order by activityReleaseTime";
				$resultset = array();
	mysqli_real_query($connect, $sql);
			
	if ($result = mysqli_store_result($connect)) {
		$i = 0;			
		while ($row = mysqli_fetch_row($result)) {
			$i++;
			$activityId=$row[0];
			//userStatus
			$userStatus=1;
			$query_userStatus="select * from evaluateusertogroup where activityId='".$activityId."' and userId='".$userId."'";
			$result_status=mysqli_query($connect, $query_userStatus);
			if(mysqli_num_rows($result_status) < 1){
				$userStatus=0;
				$eachrow = array(
				'activityId' => $row[0],
				'activityName' => $row[1],
				'activityImage' => $row[2],
				'aSurplusQuota' => $row[3],
				'activityStatus' => $row[4],
				'aNeedNumOfPerson' => $row[5],
				'userStatus' => $userStatus
			);
			$resultset[$i] = $eachrow;
			}else if(mysqli_num_rows($result_status) == 1){
				$userStatus=1;
				$eachrow = array(
				'activityId' => $row[0],
				'activityName' => $row[1],
				'activityImage' => $row[2],
				'aSurplusQuota' => $row[3],
				'activityStatus' => $row[4],
				'aNeedNumOfPerson' => $row[5],
				'userStatus' => $userStatus
			);$resultset[$i] = $eachrow;
			}else{
				return Response::show(405, 'false', "the data is repeated");
			}
			
			
		}
		mysqli_free_result($result);
	}
	if($resultset) {
		return Response::show(200, 'true', $resultset);
	} else {
		return Response::show(400, 'false', 'data deliver fail');
	}
?>