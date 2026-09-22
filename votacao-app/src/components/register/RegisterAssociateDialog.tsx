import { Button } from "@/components/ui/button"
import {
  Field, FieldError,
  FieldGroup,
  FieldLabel
} from "@/components/ui/field"
import { Input } from "@/components/ui/input"
import InputMask from "@react-input/mask/InputMask"
import { Controller } from "react-hook-form"
import { useRegisterAssociateForm } from "../../hooks/useRegisterAssociateForm"
import { CardContent, CardHeader, CardTitle } from "../ui/card"
import { DialogContent } from "../ui/dialog"

export type RegisterAssociateProps = {
  setOpen: (v: boolean) => void
}

export const RegisterAssociateDialog = ({ setOpen }: RegisterAssociateProps) => {

  const { form, registerAssociateMutation, onSubmit } = useRegisterAssociateForm({ setOpen })
  const isLoading = registerAssociateMutation.isPending

  return (
    <DialogContent>
      <CardHeader>
        <CardTitle>Registre para votar</CardTitle>
      </CardHeader>
      <CardContent>
        <form onSubmit={form.handleSubmit(onSubmit)}>
          <FieldGroup>
            <Controller
              control={form.control}
              name="cpf"
              render={({ field, fieldState }) => (
                <Field data-ivalid={fieldState.invalid}>
                  <FieldLabel htmlFor="cpf">
                    CPF
                  </FieldLabel>
                  <InputMask
                    {...field}
                    id="cpf"
                    aria-invalid={fieldState.invalid}
                    replacement={{ _: /\d/ }}
                    mask="___.___.___-__"
                    placeholder="000.000.000-00"
                    component={Input} />
                  {fieldState.invalid && <FieldError errors={[fieldState.error]} />}
                </Field>
              )} />
            <FieldGroup>
              <Field>
                <Button type="submit" disabled={isLoading ?? false}>Registrar</Button>
              </Field>
            </FieldGroup>
          </FieldGroup>
        </form>
      </CardContent>
    </DialogContent>
  )
}
