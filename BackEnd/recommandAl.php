<?php
	require_once('./db.php');
//	require_once('./response.php');
	class Recommand{
		public static function getListinfo($pagesize, $connect, $userId, $activityType){	

			//return Response::show(200, 'true', $userId.$activityType);
			$sql = "select activityId,activityName,activityImage,aSurplusQuota,activityStatus,aNeedNumOfPerson, activityAttention 
			from activity 
			where activityType = '".$activityType."'
			order by activityAttention DESC limit " . $pagesize;
//			return Response::show(200, 'true', $sql);
//			
			$resultset = array(-1=>1);
			$result = mysqli_query($connect, $sql);
			
			if (mysqli_num_rows($result) !== 0) {
				//return Response::show(200, 'true', $result);
				$i = 0;			
				while ($row = mysqli_fetch_row($result)) {
					$i++;
					$activityId=$row[0];
					//userStatus
					$userStatus=1;
					$query_userStatus="select * 
					from participate 
					where activityId='".$activityId."' and userId='".$userId."'";
					$result_status=mysqli_query($connect, $query_userStatus);
					if(mysqli_num_rows($result_status) == 0){
						$userStatus=0;
					}
					
					$eachrow = array(
						'activityId' => $row[0],
						'activityName' => $row[1],
						'activityImage' => $row[2],
						'aSurplusQuota' => $row[3],
						'activityStatus' => $row[4],
						'aNeedNumOfPerson' => $row[5],
						'userStatus' => $userStatus,
						'activityAttention' => $row[6]
					);
					$resultset[$i] = $eachrow;
				}
				mysqli_free_result($result);
			}else{
				return $resultset;
			}
			//return Response::show(200, 'true', $resultset);
			
			array_multisort(array_column($resultset,'activityAttention'),SORT_DESC,$resultset);
			return $resultset;
		}
	}
?>