import http from 'k6/http';
import { check } from 'k6';

export default function () {

  const payload = JSON.stringify({
      name: 'aaa bbb',
      age: 50,
      gender: 'MALE'
  });

  const params = {
    headers: {
      'Content-Type': 'application/json',
    },
  };

  const res = http.post(`http://${__ENV.PERSONS_URI}/persons`, payload, params);

  check(res, {
    'is status 200': (res) => res.status === 200,
    'body size is > 0': (r) => r.body.length > 0,
  });
}