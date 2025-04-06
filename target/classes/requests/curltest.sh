#   !/bin/sh
echo
echo "0.list of endpoints - default Spring API"
curl localhost:8080/api
echo
echo "1.listing - default Spring API"
curl localhost:8080/api/tFGs
echo
echo "2.adding element - My API"
curl -X 'POST' \
  'http://localhost:8080/myApi/tfgs' \
  -H 'Content-Type: application/json' \
  -d '{
  "alumno": "ys.martin@alumnos.upm.es",
  "tutor": "ys.martin@upm.es",
  "titulo": "string",
  "resumen": "string",
  "estado": "SOLICITADO"
  }'
echo
echo "3.listing - My API"
curl http://localhost:8080/myApi/tfgs
echo
echo "4.remove"
curl -X 'DELETE' \
'http://localhost:8080/myApi/tfgs/ys.martin%40alumnos.upm.es'
echo
echo "5.listing"
curl http://localhost:8080/myApi/tfgs
echo
echo "0.end"
