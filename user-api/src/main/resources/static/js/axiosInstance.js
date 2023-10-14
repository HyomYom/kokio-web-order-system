const instance = axios.create();

// Response Interceptor 설정
instance.interceptors.response.use(
    function (response) {
      // 정상 응답은 그대로 반환
      return response;
    },
    async function (error) {
      const originalRequest = error.config;

      // 에러 코드가 400이고, 재요청 횟수가 설정된 값보다 작은 경우
      if (error.response.status === 400 && !originalRequest._retry) {
        originalRequest._retry = true; // 재요청 표시

        try {
          await instance.post('/auth/refresh_token');
          return instance(originalRequest); // 재요청
        } catch (refreshError) {
          console.error("Refresh token failed", refreshError);
        }
      }

      return Promise.reject(error); // 에러를 다시 던져서 외부에서 처리할 수 있게 합니다.
    }
);