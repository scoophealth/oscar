update drugs set past_med = false where past_med is null;
alter table drugs modify `past_med` boolean not null;
