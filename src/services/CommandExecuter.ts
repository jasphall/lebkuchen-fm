import Command, { CommandType, AddArgument, QueueArgument } from '../domain/Command';
import Song from '../domain/Song';
import SongRepository from '../repositories/SongRepository';
import IoConnection from '../clients/IoConnection';
import { QueueActionType } from '../domain/io-messages/QueueMessage';

function handleAdd(argument: AddArgument) {
  const song: Song = {
    name: argument.name,
    youtubeId: argument.id,
    trimStartSeconds: argument.trimStartSeconds,
    trimEndSeconds: argument.trimEndSeconds,
    timesPlayed: 0,
  };

  SongRepository.insert(song);
}

async function handleQueue(argument: QueueArgument) {
  try {
    const song = await SongRepository.getByName(argument.id);
    IoConnection.broadcast('queue', { action: QueueActionType.Add, song });
  } catch (err) {
    console.error(err);
  }
}

function execute(command: Command) {
  switch (command.type) {
    case CommandType.Add:
      handleAdd(command.arguments as AddArgument);
      break;
    case CommandType.Queue:
      handleQueue(command.arguments as QueueArgument);
      break;
    case CommandType.Skip:
      break;
  }
}

export default {
  execute,
};