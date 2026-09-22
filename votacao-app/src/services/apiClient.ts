import axios from 'axios'

const API_BASE_URL_V1 = "http://localhost:8080/api/v1"

export const apiClient = axios.create({
    baseURL: API_BASE_URL_V1 ,
    headers: {
        "Content-Type": "application/json"
    }
})
