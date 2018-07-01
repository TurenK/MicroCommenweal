<?php
class recommandForUser{
	public static function getCosV($USER_NUM, $ITEM_NUM,$CACULATE_NUM, $array){
		$cos = array();
		$cos[0] = 0;
		$fm1 = 0;
		//开始计算cos
		//计算分母1，分母1是第一个公式里面 “*”号左边的内容，分母二是右边的内容
		for($i=1;$i<$USER_NUM;$i++){
			for($i=1;$i<$ITEM_NUM;$i++){
				if($array[$USER_NUM][$i] != null){
					$fm1 += $array[$USER_NUM][$i] * $array[$USER_NUM][$i];
				}
			}
		}
		 
		$fm1 = sqrt($fm1);
		 
		for($i=0;$i<$ITEM_NUM;$i++){
			$fz = 0;
			$fm2 = 0;
			echo "Cos(".$array[$ITEM_NUM][0].",".$array[$i][0].")=";
			
			for($j=1;$j<$CACULATE_NUM;$j++){
				//计算分子
				if($array[$ITEM_NUM][$j] != null && $array[$i][$j] != null){
					$fz += $array[$ITEM_NUM][$j] * $array[$i][$j];
				}
				//计算分母2
				if($array[$i][$j] != null){
					$fm2 += $array[$i][$j] * $array[$i][$j];
				}			
			}
			$fm2 = sqrt($fm2);
			$cos[$i] = $fz/$fm1/$fm2;
			//echo $cos[$i]."<br/>";
			return $cos;
	}
		
	//对计算结果进行排序,递归快排
	public static function quicksort($str){
		if(count($str)<=1) return $str;//如果个数不大于一，直接返回
		$key=$str[0];//取一个值，稍后用来比较；
		$left_arr=array();
		$right_arr=array();
		
		for($i=1;$i<count($str);$i++){//比$key大的放在右边，小的放在左边；
			if($str[$i]>=$key)
			$left_arr[]=$str[$i];
			else
			$right_arr[]=$str[$i];
		}
		$left_arr=quicksort($left_arr);//进行递归；
		$right_arr=quicksort($right_arr);
		return array_merge($left_arr,array($key),$right_arr);//将左中右的值合并成一个数组；
	}
	 
	public static function storeCos($NEIGHBOR, $ITEM_NUM,$CACULATE_NUM, $cos){
		//$neighbour_set 存储最近邻的人和cos值
		$neighbour_set = array();
		for($i=0;$i<$NEIGHBOR;$i++){
			for($j=0;$j<$ITEM_NUM;$j++){
				if($neighbour[$i] == $cos[$j]){
					for($j=0;$j<$CACULATE_NUM;$j++)
					$neighbour_set[$i][0] = $j;
					$neighbour_set[$i][1] = $cos[$j];
				}
			}
		}
		return $neighbour_set;
	}
	
	public static function getSequenceArray($neighbour_set, $ITEM_NUM,$CACULATE_NUM){
		$p_arr = array();
		for($i=0;$i<$ITEM_NUM;$i++){
			$pfz_f = 0;
			$pfm_f = 0;
			for($i=0;$i<$CACULATE_NUM;$i++){
				$pfz_f += $neighbour_set[$i][1] * $neighbour_set[$i][2];
				$pfm_f += $neighbour_set[$i][1];
			}
			$p_arr[0][0] = $ITEM_NUM;
			$p_arr[0][1] = $pfz_f/sqrt($pfm_f);
		}
	}
}
