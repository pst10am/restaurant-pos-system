package resrc;

import refx.UnitType;
import model.DecDegree;

@SuppressWarnings("unused")
public class TestResrc {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		{
			ResData.status();
		}
		DecDegree destPnt = DecDegree.newInstance(41.910202, -87.678730);
		System.out.printf("distance = [%f]miles [%f]km",
				ResUtil.getDistance(ResCfg.getShopDecDegree(), destPnt, ResCfg.defaultUnitType()),
				ResUtil.getDistance(ResCfg.getShopDecDegree(), destPnt, UnitType.Metric)
			
			)
			;
			*/
		double v1 = 45.8321249999;
		double v2 = Math.round(v1 * 100.0) / 100.0;
		System.out.printf("[%f] [%f]\n", v1, v2);
		
		System.out.println(ResData.currentYear());
	}

}
