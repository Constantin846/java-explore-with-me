/*CREATE OR REPLACE FUNCTION distance(lat1 float, lon1 float, lat2 float, lon2 float)
    RETURNS float AS '
    declare
        dist float = 0;
        rad_lat1 float;
        rad_lat2 float;
        theta float;
        rad_theta float;
    BEGIN
        IF lat1 = lat2 AND lon1 = lon2
        THEN
            RETURN dist;
        ELSE
            -- переводим градусы широты в радианы
            rad_lat1 = pi() * lat1 / 180;
            -- переводим градусы долготы в радианы
            rad_lat2 = pi() * lat2 / 180;
            -- находим разность долгот
            theta = lon1 - lon2;
            -- переводим градусы в радианы
            rad_theta = pi() * theta / 180;
            -- находим длину ортодромии
            dist = sin(rad_lat1) * sin(rad_lat2) + cos(rad_lat1) * cos(rad_lat2) * cos(rad_theta);

            IF dist > 1
                THEN dist = 1;
            END IF;

            dist = acos(dist);
            -- переводим радианы в градусы
            dist = dist * 180 / pi();
            -- переводим градусы в километры
            dist = dist * 60 * 1.8524;

            RETURN dist;
        END IF;
    END;
' LANGUAGE PLPGSQL;*/

CREATE ALIAS distance AS '
    Double distance(Double lat, Double lon, Double locationLat, Double locationLon) {
        double rad_lat1 = lat * Math.PI / 180;
        double rad_lat2 = locationLat * Math.PI / 180;
        double rad_lon1 = lon * Math.PI / 180;
        double rad_lon2 = locationLon * Math.PI / 180;

        if (rad_lat1 > rad_lat2) {
            if (rad_lon1 > rad_lon2) {
                return calculateDistance(rad_lat1, rad_lat2, rad_lon1, rad_lon2);
            } else {
                return calculateDistance(rad_lat2, rad_lat1, rad_lon1, rad_lon2);
            }
        } else {
            if (rad_lon1 > rad_lon2) {
                return calculateDistance(rad_lat1, rad_lat2, rad_lon2, rad_lon1);
            } else {
                return calculateDistance(rad_lat2, rad_lat1, rad_lon2, rad_lon1);
            }
        }
    }

    static Double calculateDistance(double latMax, double latMin, double lonMax, double lonMin) {
        double radius = 6371;
        double deltaLat = 2 * radius * Math.sin((latMax - latMin) / 2);
        double deltaLonMin = 2 * radius * Math.cos(latMax) * Math.sin((lonMax - lonMin) / 2);
        double deltaLonMax = 2 * radius * Math.cos(latMin) * Math.sin((lonMax - lonMin) / 2);
        double deferenceDeltaLon = (deltaLonMax - deltaLonMin) / 2;
        double lengthBetweenLatSqrt = Math.pow(deltaLat, 2) - Math.pow(deferenceDeltaLon, 2);
        double length = Math.sqrt(lengthBetweenLatSqrt + Math.pow(deltaLonMax - deferenceDeltaLon, 2));
        double alfa = 2 * Math.asin(length / (2 * radius));
        return alfa * radius;
    }
';
