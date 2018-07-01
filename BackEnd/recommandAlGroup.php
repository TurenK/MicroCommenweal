<?php
	require_once('./db.php');
//	require_once('./response.php');
	class RecommandGroup{
		public static function getListinfo($pagesize, $connect, $groupId, $activityType){			
			$sql = "select activityId,activityName,activityImage,aSurplusQuota,activityStatus,aNeedNumOfPerson 
			from activity 
			where activityType = '".$activityType."'
			order by activityReleaseTime limit " . $pagesize;
//			return Response::show(200, 'true', $sql);
//			
			$resultset = array();
			mysqli_real_query($connect, $sql);
			
			if ($result = mysqli_store_result($connect)) {
//				return Response::show(200, 'true', $result);
				$i = 0;			
				while ($row = mysqli_fetch_row($result)) {
					$i++;
					$activityId=$row[0];
					
					$eachrow = array(
						'activityId' => $row[0],
						'activityName' => $row[1],
						'activityImage' => $row[2],
						'aSurplusQuota' => $row[3],
						'activityStatus' => $row[4],
						'aNeedNumOfPerson' => $row[5]
					);
					$resultset[$i] = $eachrow;
				}
				mysqli_free_result($result);
			}
			return $resultset;
		}
	}
?>