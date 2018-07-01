//计算Leo对f的评分
$p_arr = array();
$pfz_f = 0;
$pfm_f = 0;
for($i=0;$i<3;$i++){
	$pfz_f += $neighbour_set[$i][1] * $neighbour_set[$i][2];
	$pfm_f += $neighbour_set[$i][1];
}
$p_arr[0][0] = 6;
$p_arr[0][1] = $pfz_f/sqrt($pfm_f);
if($p_arr[0][1]>3){
	echo "推荐f";
}
 
//计算Leo对g的评分
$pfz_g = 0;
$pfm_g = 0;
for($i=0;$i<3;$i++){
	$pfz_g += $neighbour_set[$i][1] * $neighbour_set[$i][3];
	$pfm_g += $neighbour_set[$i][1];
	$p_arr[1][0] = 7;
	$p_arr[1][1] = $pfz_g/sqrt($pfm_g);
}
if($p_arr[0][1]>3){
	echo "推荐g";
}
 
//计算Leo对h的评分
$pfz_h = 0;
$pfm_h = 0;
for($i=0;$i<3;$i++){
	$pfz_h += $neighbour_set[$i][1] * $neighbour_set[$i][4];
	$pfm_h += $neighbour_set[$i][1];
	$p_arr[2][0] = 8;
	$p_arr[2][1] = $pfz_h/sqrt($pfm_h);
}
print_r($p_arr);
if($p_arr[0][1]>3){
	echo "推荐h";
}
