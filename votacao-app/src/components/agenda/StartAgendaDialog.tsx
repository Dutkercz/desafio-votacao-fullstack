import { type BaseSyntheticEvent } from 'react'
import { DialogClose, DialogContent, DialogDescription, DialogFooter, DialogHeader, DialogTitle } from '../ui/dialog'
import { Field, FieldGroup } from '../ui/field'
import { Label } from '../ui/label'
import { Input } from '../ui/input'
import { Button } from '../ui/button'
import { Separator } from '../ui/separator'
import { useAgendaDialog } from '@/hooks/useAgendaDialog'

const StartAgendaDialog = ({ setOpen }: { setOpen: (v: boolean) => void }) => {

  const { submitAgenda, isPending, title, setTitle } = useAgendaDialog({ setOpen })

  return (
    <DialogContent className="sm:max-w-xl">
      <DialogHeader className='text-center'>
        <DialogTitle>Adicionar Pauta</DialogTitle>
        <DialogDescription>adicione uma pauta a ser votada</DialogDescription>
      </DialogHeader>
      <Separator orientation='horizontal' />
      <FieldGroup className='mt-2'>
        <Field>
          <Label htmlFor="title">Titulo da Pauta</Label>
          <Input
            id="title"
            name="title"
            value={title}
            onChange={(e: BaseSyntheticEvent) => setTitle(e.target.value)}
            placeholder="Aprovar compra de mesas" />
        </Field>
      </FieldGroup>
      <DialogFooter>
        <DialogClose render={<Button variant="outline">Cancelar</Button>} />
        <Button type="submit" disabled={isPending}
          onClick={submitAgenda}>Adicionar</Button>
      </DialogFooter>
    </DialogContent>
  )
}

export default StartAgendaDialog
