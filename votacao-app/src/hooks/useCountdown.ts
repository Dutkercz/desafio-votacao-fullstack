import { sessionService } from "@/services/sessionService"
import type { BackendError } from "@/types/backendError"
import { onError } from "@/utils/onError"
import { useMutation, useQueryClient } from "@tanstack/react-query"
import type { AxiosError } from "axios"
import { useCallback, useState, useEffect } from "react"


export const useCountdown = (endDate: string) => {
  const queryClient = useQueryClient()

  const getRemainingSeconds = useCallback(() => {
    const diffMs = new Date(endDate).getTime() - Date.now()
    return Math.max(0, Math.ceil(diffMs / 1000))
  }, [endDate])

  const [countdown, setCountdown] = useState<number>(() => getRemainingSeconds())

  useEffect(() => {
    const setData = () => {
      setCountdown(getRemainingSeconds())
    }
    setData()

    if (countdown <= 0) {
      return

    }

    const intervalo = setInterval(() => {
      setCountdown(() => getRemainingSeconds())
    }, 1000)

    return () => clearInterval(intervalo)
  }, [countdown, getRemainingSeconds])


  const endSessionMutation = useMutation({
    mutationFn: (id: number) => sessionService.endSession(id),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["agendas"] })
      queryClient.invalidateQueries({ queryKey: ["agendas-result"] })
    },
    onError: (error: AxiosError<BackendError>) => {
      onError(error)
    },
  })

  const handleSubmit = (id: number) => {
    endSessionMutation.mutate(id)
  }

  const minutes = Math.floor(countdown / 60)
  const seconds = (countdown % 60).toString().padStart(2, "0")
  const isPending = endSessionMutation.isPending

  return { minutes, seconds, countdown, handleSubmit, isPending }
}