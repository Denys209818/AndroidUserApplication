using AndroidUserApplication.Data.Identity.Entities;
using AndroidUserApplication.Models;
using AutoMapper;
using System.IO;

namespace AndroidUserApplication.Mappers
{
    public class IdentityMapper : Profile
    {
        public IdentityMapper()
        {
            //Маппер який конвертує модель для реєстрації у сущность БД
            CreateMap<RegisterUserModel, AppUser>()
                .ForMember(x => x.Email, y => y.MapFrom(z => z.Email))
                .ForMember(x => x.UserName, y => y.MapFrom(z => z.Email))
                .ForMember(x => x.FirstName, y => y.MapFrom(z => z.FirstName))
                .ForMember(x => x.LastName, y => y.MapFrom(z => z.LastName))
                .ForMember(x => x.Phone, y => y.MapFrom(z => z.Phone))
                .ForMember(x => x.Image, y => y.MapFrom(z => z.Image));
            //Маппер який конвертує сущность БД у модель користувача
            CreateMap<AppUser, UserModel>()
                .ForMember(x => x.Email, y => y.MapFrom(z => z.Email))
                .ForMember(x => x.FirstName, y => y.MapFrom(z => z.FirstName))
                .ForMember(x => x.LastName, y => y.MapFrom(z => z.LastName))
                .ForMember(x => x.Phone, y => y.MapFrom(z => z.Phone))
                .ForMember(x => x.Image, y => y.MapFrom(z => "/Images/"+ z.Image));
            //Маппер який конвертує модель для редагування у сущность БД
            CreateMap<UserEditModel, AppUser>()
                .ForMember(x => x.FirstName, y => y.MapFrom(z => z.FirstName))
                .ForMember(x => x.Email, y => y.MapFrom(z => z.Email))
                .ForMember(x => x.LastName, y => y.MapFrom(z => z.LastName))
                .ForMember(x => x.Phone, y => y.MapFrom(z => z.Phone))
                .ForMember(x => x.Image, y => y.Ignore());
                
        }
    }
}
