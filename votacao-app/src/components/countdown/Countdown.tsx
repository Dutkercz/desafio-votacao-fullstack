import type { SessionResponseDto } from "@/types/session";
import { useCountdown } from "../../hooks/useCountdown";
import { useEffect } from "react";

type CountDownTimeProps = {
  session: SessionResponseDto
}

export const Countdown = ({ session }: CountDownTimeProps) => {

  const { minutes, seconds, countdown, handleSubmit, isPending } = useCountdown(session.fim)

  useEffect(() => {
    if (countdown <= 0 && session.status === "EM_ANDAMENTO" && !isPending) {
      handleSubmit(session.id);
    }
  }, [countdown, session.status, session.id, isPending, handleSubmit]);

  return (
    <div>
      {countdown > 0 ? (
        <strong>
          <p> Tempo restante

            <span> {minutes}:{seconds} </span>
          </p>
        </strong>
      ) : (
        <strong className="text-destructive/50">Votação finalizada</strong>
      )}
    </div>
  )
}
